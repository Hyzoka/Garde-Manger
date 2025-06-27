package com.garde.domain.repo

import com.garde.domain.model.Product
import com.garde.domain.model.ProductDetails
import com.garde.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductByBarcode(barcode: String): Result<Product>
    suspend fun getProductDetails(barcode: String): Result<ProductDetails>
    fun getAllProducts(): Flow<ResultState<List<Product>>> // refacto
    suspend fun saveProduct(product: Product): Result<Unit>
    suspend fun updateProductQuantity(productId: String, newQuantity: Int): Result<Unit>
    suspend fun deleteProduct(productId: String): Result<Unit>


}
