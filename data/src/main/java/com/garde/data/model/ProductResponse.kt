package com.garde.data.model

import com.garde.domain.model.Product
import com.garde.domain.model.ProductLot
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("product") val product: ProductDto?
)

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("product_name") val name: String,
    @SerializedName("brands") val brand: String?,
    @SerializedName("image_url") val imageUrl: String?
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

fun ProductEntity.toLot(): ProductLot {
    return ProductLot(
        expirationDate = expirationDate,
        quantity = quantity
    )
}

