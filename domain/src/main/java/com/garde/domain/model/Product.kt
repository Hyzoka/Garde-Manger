package com.garde.domain.model

data class Product(
    val id: String,
    val barcode: String,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String?,
    val quantity: Int?,
)
