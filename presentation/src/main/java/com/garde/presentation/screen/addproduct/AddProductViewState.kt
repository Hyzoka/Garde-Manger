package com.garde.presentation.screen.addproduct

import com.garde.domain.model.Product

data class AddProductViewState(
    val step: AddProductStep = AddProductStep.ScanBarcode,
    val scannedBarcode: String? = null,
    val product: Product? = null,
    val expirationDate: String? = null,
    val isBottomSheetVisible: Boolean = false
)