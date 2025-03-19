package com.garde.presentation.screen.addproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.usecase.ExtractExpirationDateUseCase
import com.garde.domain.usecase.GetProductUseCase
import com.garde.domain.usecase.SaveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            val product = getProductUseCase(barcode)
            if (product != null) {
                _viewState.value = _viewState.value.copy(
                    step = AddProductStep.Confirmation,
                    scannedBarcode = barcode,
                    product = product,
                    isBottomSheetVisible = true
                )
            }
        }
    }

    fun processScannedText(text: String) {
        viewModelScope.launch {
            Log.i("AddProductViewModel", "Scanned text: $text")

            val detectedDate = extractExpirationDateUseCase(text)
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
                _viewState.value = _viewState.value.copy(quantity = "1", isQuantityError = false)
            } else {
                _viewState.value =
                    _viewState.value.copy(quantity = newQuantity, isQuantityError = false)
            }
        } else {
            _viewState.value = _viewState.value.copy(quantity = "", isQuantityError = true)
        }
        validateForm()
    }

    private fun validateForm() {
        val state = _viewState.value
        val isValid = state.scannedBarcode != null &&
                state.expirationDate != null &&
                state.quantity.toIntOrNull()?.let { it > 0 } ?: false

        _viewState.value = state.copy(isSaveEnabled = isValid)
    }

    fun saveProduct() {
        val state = _viewState.value
        val barcode = state.scannedBarcode ?: return
        val expirationDate = state.expirationDate ?: return
        val quantity = state.quantity.toIntOrNull()?.takeIf { it > 0 } ?: return

        state.product?.let { product ->
            viewModelScope.launch {
                saveProductUseCase(
                    barcode = barcode,
                    name = product.name ?: "Unknown",
                    brand = product.brand,
                    imageUrl = product.imageUrl,
                    expirationDate = expirationDate,
                    quantity = quantity
                )
                _viewState.value =
                    _viewState.value.copy(isBottomSheetVisible = false, step = AddProductStep.Saved)
            }
        }
    }
}