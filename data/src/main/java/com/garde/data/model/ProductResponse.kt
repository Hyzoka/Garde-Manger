package com.garde.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("product") val product: ProductDto?
)

@Serializable
data class ProductDto(
    @SerialName("id") val id: String,
    @SerialName("product_name") val name: String,
    @SerialName("brands") val brand: String? = null,
    @SerialName("image_url") val imageUrl: String? = null
)


