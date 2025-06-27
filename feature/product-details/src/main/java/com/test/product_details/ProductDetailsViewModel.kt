package com.test.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val barcode: String = checkNotNull(savedStateHandle["barcode"]) {
        "barcode is required"
    }

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchProductDetails()
    }

    private fun fetchProductDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getProductDetails(barcode)
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            product = details.product,
                            lots = details.lots
                        )
                    }
                }
                .onFailure { ex ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ex.message ?: "Unknown error"
                        )
                    }
                }
        }
    }

    fun retry() {
        fetchProductDetails()
    }
}