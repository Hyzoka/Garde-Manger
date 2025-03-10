package com.garde.domain.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_name") val name: String?,
    @SerializedName("brands") val brand: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("expiration_date") val expirationDate: String?,
    @SerializedName("nutriments") val nutriments: Nutriments?,
    @SerializedName("ingredients_text") val ingredients: String?,
    @SerializedName("categories") val categories: String?
)