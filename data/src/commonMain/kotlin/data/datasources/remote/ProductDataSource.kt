package data.datasources.remote

import data.model.ProductResponse

interface ProductDataSource {

    suspend fun getProductByBarcode(barcode: String): Result<ProductResponse>
    // suspend fun getProductDetails(barcode: String): Result<ProductDetails>
}