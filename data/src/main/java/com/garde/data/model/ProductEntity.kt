package com.garde.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.garde.domain.model.Product
import java.util.UUID

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val barcode: String,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String?,
    val quantity: Int
)


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
