package com.garde.data.remote

import com.garde.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsService {
    @GET("api/v2/product/{barcode}")
    suspend fun getProduct(@Path("barcode") barcode: String): ProductResponse
}
