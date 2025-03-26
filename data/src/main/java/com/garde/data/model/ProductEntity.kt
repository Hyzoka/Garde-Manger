package com.garde.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.garde.domain.model.Product

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val barcode: String,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String?,
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
        expirationDate = expirationDate,
        quantity = quantity
    )
}
