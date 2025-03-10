package com.garde.domain.repo

import com.garde.domain.model.Product
import com.garde.domain.remote.OpenFoodFactsService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: OpenFoodFactsService
) {

    suspend fun getProduct(barcode: String): Product? {
        return try {
            val response = api.getProductInfo(barcode)
            response.product
        } catch (e: Exception) {
            null
        }
    }
}