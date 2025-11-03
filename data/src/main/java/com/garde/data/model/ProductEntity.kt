package com.garde.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
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
