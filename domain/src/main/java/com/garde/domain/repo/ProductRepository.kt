package com.garde.domain.repo

import com.garde.domain.utils.ResultState
import com.garde.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductByBarcode(barcode: String): Result<Product>
    fun getProductDetailsById(id: Int): Flow<ResultState<Product>> // refacto
    fun getAllProducts(): Flow<ResultState<List<Product>>> // refacto
    suspend fun saveProduct(product: Product): Result<Unit>
    suspend fun updateProductQuantity(productId: String, expirationDate: String, newQuantity: Int): Result<Unit>
    suspend fun deleteProduct(productId: String, expirationDate: String): Result<Unit>


}
