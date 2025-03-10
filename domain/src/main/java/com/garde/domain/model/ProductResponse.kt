package com.garde.domain.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("product") val product: Product?
)