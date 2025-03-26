package com.garde.data.model

import com.google.gson.annotations.SerializedName

data class Nutriments(
    @SerializedName("energy_kcal") val calories: Double?,
    @SerializedName("proteins_100g") val proteins: Double?,
    @SerializedName("fat_100g") val fat: Double?,
    @SerializedName("carbohydrates_100g") val carbohydrates: Double?,
    @SerializedName("sugars_100g") val sugars: Double?,
    @SerializedName("fiber_100g") val fiber: Double?,
    @SerializedName("salt_100g") val salt: Double?
)