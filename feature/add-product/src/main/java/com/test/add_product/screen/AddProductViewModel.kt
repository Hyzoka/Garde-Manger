package com.test.add_product.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garde.domain.repo.ProductRepository
import com.garde.domain.usecase.ExtractExpirationDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
) : ViewModel() {

    private val productIntent = AddProductIntent(repository)

    private val _productState = MutableStateFlow(AddProductViewState())
    val productState: StateFlow<AddProductViewState> = _productState

    val step: StateFlow<AddProductStepViewState>
        get() = _productState.map {
            if (it.product == null) {
                AddProductStepViewState.ScanProduct
            } else if (it.product.quantity == null) {
                AddProductStepViewState.SelectQuantity
            } else if (it.product.expirationDate == null) {
                AddProductStepViewState.ScanExpirationDate
            } else {
                AddProductStepViewState.ConfirmProduct
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AddProductStepViewState.ScanProduct
        )

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen: SharedFlow<Unit>
        get() = _closeScreen

    private var fetchingProductJob: Job? = null

    fun fetchProduct(barcode: String) {
        fetchingProductJob?.cancel()
        fetchingProductJob = viewModelScope.launch {
            delay(500)
            val stateWithProduct = productIntent.intent(
                intent = AddProductIntentInterface.GetProduct(barcode = barcode),
                state = productState.value
            )
            _productState.update { stateWithProduct }
        }
    }

    fun handleExpirationDate(expirationDate: String) {
        viewModelScope.launch {
            val detectedDate = extractExpirationDateUseCase.invoke(expirationDate)
            if (detectedDate != null) {
                _productState.update { it.copy(product = it.product?.copy(expirationDate = detectedDate)) }
            }
        }
    }

    fun updateQuantity(newQuantityText: String) {
        val newQuantity = newQuantityText.toIntOrNull()

        if (newQuantity == null || newQuantity == 0) {
            _productState.update { it.copy(isQuantityError = true) }
        } else {
            _productState.update {
                it.copy(
                    isQuantityError = false,
                    product = it.product?.copy(quantity = newQuantity.toInt())
                )
            }
        }
    }

    fun saveProduct() {
        _productState.value.product?.let { product ->
            viewModelScope.launch {
                repository.saveProduct(product = product)
                    .onSuccess {
                        _closeScreen.emit(Unit)
                    }
                    .onFailure {
                        println("Save product failed : $it")
                    }
            }
        }
    }
}
