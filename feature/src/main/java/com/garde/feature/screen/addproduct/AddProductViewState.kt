package com.garde.feature.screen.addproduct

import com.garde.domain.model.Product

data class AddProductViewState(
    val step: AddProductStep = AddProductStep.ScanBarcode,
    val scannedBarcode: String? = null,
    val quantity: String = "1",
    val product: Product? = null,
    val expirationDate: String? = null,
    val isBottomSheetVisible: Boolean = false,
    val isQuantityError: Boolean = false,
    val isSaveEnabled: Boolean = false
)