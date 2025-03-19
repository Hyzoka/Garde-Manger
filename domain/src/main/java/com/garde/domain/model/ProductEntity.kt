package com.garde.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val barcode: String, // ⚠️ Code-barres unique
    val name: String,
    val brand: String?,
    val imageUrl: String?,
    val expirationDate: String?,
    val quantity: Int = 1 // ✅ Ajout de la quantité par défaut à 1
)