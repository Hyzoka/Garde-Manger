package data.model

import kotlinx.serialization.SerialName

data class Nutriments(
    @SerialName("energy_kcal") val calories: Double?,
    @SerialName("proteins_100g") val proteins: Double?,
    @SerialName("fat_100g") val fat: Double?,
    @SerialName("carbohydrates_100g") val carbohydrates: Double?,
    @SerialName("sugars_100g") val sugars: Double?,
    @SerialName("fiber_100g") val fiber: Double?,
    @SerialName("salt_100g") val salt: Double?
)
