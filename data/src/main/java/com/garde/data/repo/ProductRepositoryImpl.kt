package com.garde.data.repo

import com.garde.data.local.ProductDao
import com.garde.data.model.toDomain
import com.garde.data.model.toEntity
import com.garde.data.remote.OpenFoodFactsService
import com.garde.domain.utils.ResultState
import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: OpenFoodFactsService,
    private val productDao: ProductDao
) : ProductRepository  {

    //fetch product from api with bare-code
    override suspend fun getProductByBarcode(barcode: String): Result<Product> = runCatching {
        withContext(Dispatchers.IO) {
            apiService.getProduct(barcode).product?.toDomain() ?: error("Product cannot be retrieved with barcode : $barcode")
        }
    }

    override fun getProductDetailsById(id: Int): Flow<ResultState<Product>> {
        TODO("Not yet implemented")
    }

    override fun getAllProducts(): Flow<ResultState<List<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            val products = productDao.getAllProducts().map { list -> list.map { it.toDomain() } }
            emitAll(products.map { ResultState.Success(it) })
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Erreur inconnue"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun saveProduct(product: Product): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val result = productDao.insertProduct(product.toEntity())
            if (result == -1L) {
                productDao.incrementQuantity(product.id, product.quantity ?: error("No quantity specified for the product"))
            }
        }
    }
}
