package com.garde.presentation.screen.addproduct

sealed class AddProductStep {
    object ScanBarcode : AddProductStep()
    object ScanExpirationDate : AddProductStep()
    object Confirmation : AddProductStep()
}