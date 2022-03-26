package com.application.nikestore.data.db

import androidx.room.*
import com.application.nikestore.data.entity.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorite(product: Product): Completable

    @Delete
    fun deleteFromFavorites(product: Product): Completable

    @Query("SELECT * FROM favorite")
    fun getFavoriteProducts(): Single<List<Product>>
}