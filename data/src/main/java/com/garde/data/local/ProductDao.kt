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
    suspend fun insertProduct(product: ProductEntity): Long // ⚠️ Retourne `-1` si le produit existe déjà

    @Query("UPDATE product SET quantity = quantity + :amount WHERE barcode = :barcode")
    suspend fun incrementQuantity(barcode: String, amount: Int)

    @Query("SELECT * FROM product WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM product")
    fun getAllProducts(): Flow<List<ProductEntity>>
}


