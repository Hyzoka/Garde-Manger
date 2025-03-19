package com.garde.domain.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.garde.domain.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductEntity): Long // ⚠️ Retourne `-1` si le produit existe déjà

    @Query("UPDATE products SET quantity = quantity + :amount WHERE barcode = :barcode")
    suspend fun incrementQuantity(barcode: String, amount: Int)

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

}
