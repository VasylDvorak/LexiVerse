package com.diplomproject.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diplomproject.room.favorite.FavoriteDao
import com.diplomproject.room.favorite.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
