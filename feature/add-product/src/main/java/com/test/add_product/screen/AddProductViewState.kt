package com.test.add_product.screen

import com.garde.domain.model.Product

data class AddProductViewState(
    val product: Product? = null,
    val isQuantityError: Boolean = false,
)

sealed interface AddProductStepViewState {
    data object ScanProduct: AddProductStepViewState
    data object SelectQuantity: AddProductStepViewState
    data object ScanExpirationDate: AddProductStepViewState
    data object ConfirmProduct : AddProductStepViewState
}
