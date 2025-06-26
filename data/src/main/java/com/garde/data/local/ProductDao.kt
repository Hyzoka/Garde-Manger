package com.garde.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.garde.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Query("""
    SELECT * FROM products 
    WHERE barcode = :barcode AND expirationDate = :expirationDate
    LIMIT 1
""")
    suspend fun findProductByBarcodeAndDate(barcode: String, expirationDate: String): ProductEntity?

    @Query("""
    UPDATE products 
    SET quantity = quantity + :amount 
    WHERE id = :id
""")
    suspend fun incrementQuantity(id: String, amount: Int)


    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET quantity = :quantity WHERE id = :productId")
    suspend fun updateQuantity(productId: String, quantity: Int)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: String)


}


