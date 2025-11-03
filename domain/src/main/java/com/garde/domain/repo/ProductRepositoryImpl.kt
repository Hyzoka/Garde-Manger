package com.garde.domain.repo

import com.garde.data.datasources.local.ProductDao
import com.garde.data.datasources.remote.ProductDataSource
import com.garde.domain.model.Product
import com.garde.domain.model.toDomain
import com.garde.domain.model.toEntity
import com.garde.domain.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductDataSource,
    private val productDao: ProductDao
) : ProductRepository {

    //fetch product from api with bare-code
    override suspend fun getProductByBarcode(barcode: String): Result<Product> = runCatching {
        return remoteDataSource.getProductByBarcode(barcode).map { productResponse ->
            productResponse.product?.toDomain()
                ?: error("Product cannot be retrieved with barcode : $barcode")
        }
    }

    // override suspend fun getProductDetails(barcode: String): Result<ProductDetails> = runCatching {
    //     withContext(Dispatchers.IO) {
    //         // 1️⃣ API
    //         val productResponse = remoteDataSource.getProduct(barcode)
    //         val product =
    //             productResponse.product?.toDomain() ?: throw Exception("Invalid API response")
//
    //         // 2️⃣ DB
    //         val localLots = productDao.getLotsForBarcode(barcode)
    //             .first()     // 👈 transforme Flow en List
    //             .map { it.toLot() }
//
    //         // 3️⃣ Combine
    //         ProductDetails(product, localLots)
    //     }
    // }


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
            val existing = productDao.findProductByBarcodeAndDate(
                product.barcode,
                product.expirationDate ?: return@withContext
            )

            if (existing != null) {
                productDao.incrementQuantity(existing.id, product.quantity ?: 1)
            } else {
                productDao.insertProduct(product.toEntity())
            }
        }
    }


    override suspend fun updateProductQuantity(productId: String, newQuantity: Int): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                productDao.updateQuantity(productId, newQuantity)
            }
        }

    override suspend fun deleteProduct(productId: String): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                productDao.deleteProduct(productId)
            }
        }
}