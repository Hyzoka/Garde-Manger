package com.garde.feature.screen.addproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.onError
import com.garde.domain.onSuccess
import com.garde.domain.usecase.ExtractExpirationDateUseCase
import com.garde.domain.usecase.GetProductUseCase
import com.garde.domain.usecase.SaveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val extractExpirationDateUseCase: ExtractExpirationDateUseCase,
    private val saveProductUseCase: SaveProductUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(AddProductViewState())
    val viewState: StateFlow<AddProductViewState> = _viewState

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            getProductUseCase.invoke(barcode).collectLatest { result ->
                result
                    .onSuccess { product ->
                        Log.i("AddProductViewModel", "Product: $product")
                        _viewState.value = _viewState.value.copy(
                            product = product,
                            step = AddProductStep.Confirmation,
                            isBottomSheetVisible = true
                        )
                    }
                    .onError { message ->
                        Log.e("AddProductViewModel", "Error: $message")
                    }
            }
        }
    }

    fun processScannedText(text: String) {
        viewModelScope.launch {
            Log.i("AddProductViewModel", "Scanned text: $text")
            val detectedDate = extractExpirationDateUseCase.invoke(text)
            if (detectedDate != null) {
                Log.i("AddProductViewModel", "Detected expiration date: $detectedDate")
                _viewState.value = _viewState.value.copy(
                    step = AddProductStep.Confirmation,
                    expirationDate = detectedDate,
                    isBottomSheetVisible = true
                )
            }
        }
    }

    fun startExpirationDateScan() {
        _viewState.value = _viewState.value.copy(
            step = AddProductStep.ScanExpirationDate,
            isBottomSheetVisible = false
        )
    }


    fun updateQuantity(newQuantity: String) {
        if (newQuantity.toIntOrNull() != null) {
            if (newQuantity.toIntOrNull() == 0) {
                _viewState.value =
                    _viewState.value.copy(quantity = "1", isQuantityError = false)
            } else {
                _viewState.value =
                    _viewState.value.copy(quantity = newQuantity, isQuantityError = false)
            }
        } else {
            _viewState.value = _viewState.value.copy(quantity = "", isQuantityError = true)
        }
        validateForm()
    }

    fun validateForm() {
        _viewState.value.apply {
            val isValid = scannedBarcode != null &&
                    expirationDate != null &&
                    quantity.toIntOrNull()?.let { it > 0 } ?: false
            _viewState.update { it.copy(isSaveEnabled = isValid) }
        }
    }

    fun saveProduct() {
        if (_viewState.value.isSaveEnabled) {
            _viewState.value.product?.let { product ->
                viewModelScope.launch {
                    saveProductUseCase.invoke(product).collect { result ->
                        Log.i("AddProductViewModel", "Product saved: $result")
                        result.onSuccess {
                            _viewState.value = _viewState.value.copy(
                                step = AddProductStep.Saved,
                                isBottomSheetVisible = false
                            )

                        }.onError {
                            Log.e("AddProductViewModel", "Error: $it")
                        }
                    }
                }
            }
        }
    }
}