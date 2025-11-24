package data.datasources.local

import kotlinx.coroutines.flow.Flow

//interface ProductLocalDataSource {
//    suspend fun insertProduct(product: Product): Long
//    suspend fun findProductByBarcodeAndDate(barcode: String, expirationDate: String): Product?
//    suspend fun incrementQuantity(id: String, amount: Int)
//    suspend fun getProductById(id: String): Product?
//    fun getAllProducts(): Flow<List<Product>>
//    fun getLotsForBarcode(barcode: String): Flow<List<Product>>
//    suspend fun updateQuantity(productId: String, quantity: Int)
//    suspend fun deleteProduct(productId: String)
//}