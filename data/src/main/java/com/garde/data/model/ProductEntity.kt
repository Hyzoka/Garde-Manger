package com.garde.data.model

import androidx.room.Entity
import com.garde.domain.model.Product

@Entity(
    tableName = "products",
    primaryKeys = ["barcode", "expirationDate"]
)
data class ProductEntity(
    val barcode: String,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String,
    val quantity: Int
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = barcode,
        name = name,
        brand = brand,
        imageUrl = imageUrl,
        expirationDate = expirationDate,
        quantity = quantity
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        barcode = id,
        name = name,
        brand = brand,
        imageUrl = imageUrl,
        expirationDate = expirationDate ?: error("No expiration date specified in the product"),
        quantity = quantity ?: error("No quantity specified in the product")
    )
}
