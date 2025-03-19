package com.garde.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.model.ProductEntity
import com.garde.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ProductListViewState())
    val viewState: StateFlow<ProductListViewState> = _viewState

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            getProductsUseCase.invoke().onStart {
                _viewState.update { it.copy(isLoading = true) }
            }
                .catch { e ->
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Une erreur s'est produite"
                        )
                    }
                }
                .collect { products ->
                    _viewState.update { it.copy(products = products, isLoading = false) }
                }
        }
    }
}

data class ProductListViewState(
    val isLoading: Boolean = true,
    val products: List<ProductEntity> = emptyList(),
    val error: String? = null
)
