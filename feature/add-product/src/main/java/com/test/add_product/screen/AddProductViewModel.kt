package com.test.add_product.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.core.R
import com.garde.domain.repo.ProductRepository
import com.garde.domain.usecase.ExtractExpirationDateUseCase
import com.garde.domain.usecase.ValidateExpirationDateUseCase
import com.garde.domain.utils.DateValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
        private val repository: ProductRepository,
    private val extractExpirationDateUseCase: ExtractExpirationDateUseCase,
    private val validateExpirationDateUseCase: ValidateExpirationDateUseCase
) : ViewModel() {

    private var lastScannedBarcode: String? = null
    private var lastScanTime: Long = 0L
    private var expirationScanStarted = false


    private val _productState = MutableStateFlow(AddProductViewState())
    val productState: StateFlow<AddProductViewState> = _productState

    val step: StateFlow<AddProductStepViewState>
        get() = _productState.map {
            when {
                it.product == null -> AddProductStepViewState.ScanProduct
                it.product.quantity == null -> AddProductStepViewState.SelectQuantity
                it.product.expirationDate == null -> {
                    if (!expirationScanStarted) {
                        expirationScanStarted = true
                        startExpirationScanTimeout()
                    }
                    AddProductStepViewState.ScanExpirationDate
                }

                else -> AddProductStepViewState.ConfirmProduct
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AddProductStepViewState.ScanProduct
        )

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen: SharedFlow<Unit>
        get() = _closeScreen

    fun fetchProduct(barcode: String) {
        val currentTime = System.currentTimeMillis()
        if (barcode != lastScannedBarcode || currentTime - lastScanTime > 10_000L) {
            lastScannedBarcode = barcode
            lastScanTime = currentTime
            viewModelScope.launch {
                repository.getProductByBarcode(barcode)
                    .onSuccess { product ->
                        _productState.update { it.copy(product = product.copy(quantity = null)) }
                    }
                    .onFailure {
                        println("Save product failed : $it")
                    }
            }
        }

    }

    fun handleExpirationDate(expirationDate: String) {
        viewModelScope.launch {
            println("AZEAE Expiration date : $expirationDate")
            val detectedDate = extractExpirationDateUseCase.invoke(expirationDate)
            println("AZEAE Detected date : $detectedDate")
            if (detectedDate != null) {
                val validDate = validateExpirationDateUseCase.invoke(detectedDate)
                println("AZEAE validDate : $validDate")
                val errorMessage = when (validDate) {
                    DateValidationResult.InvalidDay -> R.string.error_invalid_day
                    DateValidationResult.InvalidMonth -> R.string.error_invalid_month
                    DateValidationResult.YearTooFar -> R.string.error_year_too_far
                    DateValidationResult.Expired -> R.string.error_expired_date
                    DateValidationResult.InvalidFormat -> R.string.error_invalid_date
                    else -> null
                }
                expirationScanStarted = false
                if (validDate == DateValidationResult.Valid) {
                    _productState.update {
                        it.copy(
                            product = it.product?.copy(expirationDate = detectedDate),
                            showManualExpirationDateInput = false,
                        )
                    }
                } else {
                    _productState.update {
                        it.copy(
                            showManualExpirationDateInput = true,
                            errorDateFormatMessage = errorMessage
                        )
                    }
                }
            } else {
                _productState.update {
                    it.copy(
                        showManualExpirationDateInput = true,
                    )
                }
            }
        }
    }

    fun retryExpirationScan() {
        expirationScanStarted = false
        _productState.update {
            it.copy(showManualExpirationDateInput = false, errorDateFormatMessage = null)
        }
    }


    fun updateQuantity(newQuantityText: String, saveProduct: Boolean = false) {
        val newQuantity = newQuantityText.trim().toIntOrNull()

        if (newQuantity == null || newQuantity == 0) {
            _productState.update { it.copy(isQuantityError = true) }
        } else {
            _productState.update {
                it.copy(
                    isQuantityError = false,
                    product = it.product?.copy(quantity = newQuantity.toInt())
                )
            }
            if (saveProduct) {
                saveProduct()
            }
        }
    }

    fun resetProduct() {
        _productState.update { it.copy(product = null) }
    }

    private fun saveProduct() {
        _productState.value.product?.let { product ->
            viewModelScope.launch {
                repository.saveProduct(
                    product = product
                )
                    .onSuccess {
                        _closeScreen.emit(Unit)
                    }
                    .onFailure {
                        println("Save product failed : $it")
                    }
            }
        }
    }

    private fun startExpirationScanTimeout() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(5_000L)
            if (_productState.value.product?.expirationDate == null) {
                _productState.update { it.copy(showManualExpirationDateInput = true) }
            }
        }
    }

}
