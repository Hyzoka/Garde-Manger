package com.garde.presentation.screen.addproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.usecase.ExtractExpirationDateUseCase
import com.garde.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val extractExpirationDateUseCase: ExtractExpirationDateUseCase
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

    fun saveProduct() {
        viewModelScope.launch {
            val barcode = _viewState.value.scannedBarcode ?: return@launch
            val expirationDate = _viewState.value.expirationDate ?: return@launch
            //saveProductUseCase(barcode, expirationDate)
            _viewState.value = _viewState.value.copy(
                step = AddProductStep.ScanBarcode,
                isBottomSheetVisible = false
            )
        }
    }
}