package com.garde.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.garde.data.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

}
