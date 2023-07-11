package com.diplomproject.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diplomproject.room.favorite.FavoriteDao
import com.diplomproject.room.favorite.FavoriteEntity
import com.diplomproject.room.history.HistoryDao
import com.diplomproject.room.history.HistoryEntity

@Database(
    entities = arrayOf(HistoryEntity::class, FavoriteEntity::class),
    version = 1,
    exportSchema = false
)
abstract class HistoryFavoriteDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao
}
