package com.garde.domain.repo

import com.garde.domain.ResultState
import com.garde.domain.model.Product
import kotlinx.coroutines.flow.Flow


interface ProductRepository {
    fun getProductByBarcode(barcode: String): Flow<ResultState<Product>>
    fun getProductDetailsById(id: Int): Flow<ResultState<Product>>
    fun getAllProducts(): Flow<ResultState<List<Product>>>
    fun saveProduct(product: Product): Flow<ResultState<Unit>>
}
