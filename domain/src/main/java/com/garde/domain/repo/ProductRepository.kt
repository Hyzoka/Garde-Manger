package com.garde.domain.repo

import com.garde.domain.local.ProductDao
import com.garde.domain.model.Product
import com.garde.domain.model.ProductEntity
import com.garde.domain.remote.OpenFoodFactsService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val api: OpenFoodFactsService,
    private val productDao: ProductDao,
) {

    suspend fun getProductApi(barcode: String): Product? {
        return try {
            val response = api.getProductInfo(barcode)
            response.product
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveProduct(product: ProductEntity) {
        val result = productDao.insertProduct(product)
        if (result == -1L) {
            productDao.incrementQuantity(product.barcode, product.quantity)
        }
    }

    suspend fun getProductByBarcode(barcode: String): ProductEntity? {
        return productDao.getProductByBarcode(barcode)
    }

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }
}
