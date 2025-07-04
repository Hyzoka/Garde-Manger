package com.test.product.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import com.garde.domain.usecase.GetProductsUseCase
import com.garde.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val repository: ProductRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ProductListViewState())
    val viewState: StateFlow<ProductListViewState>
        get() = _viewState.map { productViewState ->
            productViewState.copy(products = productViewState.products.filter { product ->
                product.name.uppercase().contains(productViewState.searchText.trim().uppercase())
            })
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _viewState.value
        )

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
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
            .onEach { result ->
                when (result) {
                    is ResultState.Success -> {
                        _viewState.update { it.copy(products = result.data, isLoading = false) }
                    }

                    is ResultState.Error -> {
                        _viewState.update { it.copy(error = result.message, isLoading = false) }
                    }

                    is ResultState.Loading -> {
                        _viewState.update { it.copy(isLoading = true) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun onSearchTextChange(text: String) {
        _viewState.update {
            it.copy(searchText = text)
        }
    }

    fun isEditing(isEditing: Boolean) {
        _viewState.update {
            it.copy(editStockedProduct = it.editStockedProduct.copy(isEditing = isEditing))
        }
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateProductQuantity(
                productId = product.id,
                newQuantity = newQuantity
            ).onFailure {
                // handle failure
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(
                productId = product.id,
            ).onFailure {
                // handle failure
            }
        }
    }


}
