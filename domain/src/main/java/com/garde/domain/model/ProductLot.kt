package com.garde.domain.model

import com.garde.data.model.ProductEntity

data class ProductLot(
    val expirationDate: String?,
    val quantity: Int
)


fun ProductEntity.toLot(): ProductLot {
    return ProductLot(
        expirationDate = expirationDate,
        quantity = quantity
    )
}