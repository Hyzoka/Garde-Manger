package data.datasources.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductLocalDataSourceImpl(
    private val db: AppDatabase
) : ProductLocalDataSource {

    override suspend fun insertProduct(product: Product): Long = withContext(Dispatchers.Default) {
        db.productsQueries.insert(
            id = product.id,
            name = product.name,
            barcode = product.barcode,
            quantity = product.quantity.toLong(),
            expirationDate = product.expirationDate
        )
        // SQLDelight ne retourne pas d'ID auto-incrémenté dans ce cas
        // Si besoin, ajoute une requête pour récupérer le lastInsertRowId
        0L
    }

    override suspend fun findProductByBarcodeAndDate(
        barcode: String,
        expirationDate: String
    ): Product? = withContext(Dispatchers.Default) {
        db.productsQueries
            .findByBarcodeAndDate(barcode, expirationDate)
            .executeAsOneOrNull()
            ?.toProduct()
    }

    override suspend fun incrementQuantity(id: String, amount: Int) = withContext(Dispatchers.Default) {
        db.productsQueries.incrementQuantity(amount.toLong(), id)
    }

    override suspend fun getProductById(id: String): Product? = withContext(Dispatchers.Default) {
        db.productsQueries.selectById(id).executeAsOneOrNull()?.toProduct()
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return db.productsQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toProduct() } }
    }

    override fun getLotsForBarcode(barcode: String): Flow<List<Product>> {
        return db.productsQueries.selectByBarcode(barcode)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toProduct() } }
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) = withContext(Dispatchers.Default) {
        db.productsQueries.updateQuantity(quantity.toLong(), productId)
    }

    override suspend fun deleteProduct(productId: String) = withContext(Dispatchers.Default) {
        db.productsQueries.deleteById(productId)
    }

    // Mapper SQLDelight entity -> Domain model
    private fun com.garde.data.db.Products.sq.toProduct() = Product(
        id = id,
        name = name,
        barcode = barcode,
        quantity = quantity.toInt(),
        expirationDate = expirationDate
    )
}