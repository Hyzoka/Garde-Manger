package com.garde.domain.repo

import com.garde.domain.local.ProductDao
import com.garde.domain.model.Product
import com.garde.domain.model.ProductEntity
import com.garde.domain.remote.OpenFoodFactsService
import javax.inject.Inject

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
        productDao.insertProduct(product)
    }

    suspend fun getProduct(barcode: String): ProductEntity? {
        return productDao.getProductByBarcode(barcode)
    }
}
