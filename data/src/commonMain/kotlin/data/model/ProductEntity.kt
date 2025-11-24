package data.model

import java.util.UUID

//TODO to remove

data class ProductEntity(
    val id: String = UUID.randomUUID().toString(),
    val barcode: String,
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String?,
    val quantity: Int
)
