package com.application.nikestore.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.nikestore.data.entity.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteProductDAO(): ProductDao
}