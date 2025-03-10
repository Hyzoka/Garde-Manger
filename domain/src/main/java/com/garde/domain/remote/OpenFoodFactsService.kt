package com.garde.domain.remote

import com.garde.domain.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsService {
    @GET("api/v2/product/{barcode}")
    suspend fun getProductInfo(@Path("barcode") barcode: String): ProductResponse
}