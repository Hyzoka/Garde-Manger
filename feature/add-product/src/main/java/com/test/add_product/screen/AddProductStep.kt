package com.test.add_product.screen

sealed class AddProductStep {
    object ScanBarcode : AddProductStep()
    object ScanExpirationDate : AddProductStep()
    object Confirmation : AddProductStep()
    object Saved : AddProductStep()
}