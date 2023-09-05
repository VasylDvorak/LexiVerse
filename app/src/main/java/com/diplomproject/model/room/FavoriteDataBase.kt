package com.diplomproject.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diplomproject.model.room.favorite.FavoriteDao
import com.diplomproject.model.room.favorite.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
