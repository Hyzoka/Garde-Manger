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

    @Query(
        """
        UPDATE products 
        SET quantity = quantity + :amount 
        WHERE barcode = :barcode AND expirationDate = :expirationDate
    """
    )
    suspend fun incrementQuantity(barcode: String, expirationDate: String, amount: Int)

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET quantity = :quantity WHERE barcode = :productId AND expirationDate = :expirationDate")
    suspend fun updateQuantity(productId: String, expirationDate: String, quantity: Int)

    @Query("DELETE FROM products WHERE barcode = :productId AND expirationDate = :expirationDate")
    suspend fun deleteProduct(productId: String, expirationDate: String)


}


