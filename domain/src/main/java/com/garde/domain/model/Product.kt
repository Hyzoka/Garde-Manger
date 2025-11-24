package com.garde.domain.model

import data.model.ProductDto
import data.model.ProductEntity

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

fun ProductDto.toDomain(): Product {
    return Product(
        barcode = id,
        id = id,
        name = name,
        brand = brand,
        imageUrl = imageUrl,
        expirationDate = null, // L’API ne fournit pas cette info, on la stocke dans Room
        quantity = 1
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        barcode = barcode,
        name = name,
        brand = brand,
        imageUrl = imageUrl,
        expirationDate = expirationDate,
        quantity = quantity
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        barcode = barcode,
        name = name,
        brand = brand,
        imageUrl = imageUrl,
        expirationDate = expirationDate ?: error("No expiration date specified in the product"),
        quantity = quantity ?: error("No quantity specified in the product")
    )
}