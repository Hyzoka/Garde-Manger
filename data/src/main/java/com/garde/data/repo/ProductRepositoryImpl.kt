package com.garde.data.repo

import com.garde.data.local.ProductDao
import com.garde.data.model.toDomain
import com.garde.data.model.toEntity
import com.garde.data.remote.OpenFoodFactsService
import com.garde.domain.ResultState
import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: OpenFoodFactsService,
    private val productDao: ProductDao
) : ProductRepository  {

    //fetch product from api with bare-code
    override fun getProductByBarcode(barcode: String): Flow<ResultState<Product>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.getProduct(barcode)
            val product = response.product?.toDomain()

            if (product != null) {
                emit(ResultState.Success(product))
            } else {
                emit(ResultState.Error("Produit introuvable"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Erreur inconnue"))
        }
    }.flowOn(Dispatchers.IO)

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

    override fun saveProduct(product: Product): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)

        val result = productDao.insertProduct(product.toEntity())
        if (result == -1L) {
            productDao.incrementQuantity(product.id, product.quantity)
        }

        emit(ResultState.Success(Unit))
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Erreur inconnue"))
    }.flowOn(Dispatchers.IO)
}

