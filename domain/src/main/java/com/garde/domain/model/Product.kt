package com.garde.domain.model

data class Product(
    val id: String,
    val barcode: String,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String?,
    val quantity: Int?,
    // Santé
    val nutriscore: String? = null,    // A, B, C, D, E
    val novaGroup: Int? = null,        // 1-4
    val ingredients: String? = null,
    val additives: String? = null,
    // Environnement
    val ecoscore: String? = null,      // A, B, C, D, E
    val labels: String? = null,
)
