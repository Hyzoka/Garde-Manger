package com.garde.data.datasources.remote

import com.garde.data.model.ProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductDataSourceImpl(private val client: HttpClient) : ProductDataSource {
    override suspend fun getProductByBarcode(barcode: String): Result<ProductResponse> {
        return try {
            val response = client.get("https://ton-domaine.com/api/v2/product/$barcode")
            Result.success(response.body<ProductResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // override suspend fun getProductDetails(barcode: String): Result<ProductDetails> {
    //     TODO("Not yet implemented")
    // }
}