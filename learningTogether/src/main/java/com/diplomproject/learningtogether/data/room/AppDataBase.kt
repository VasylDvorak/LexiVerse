package com.diplomproject.learningtogether.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity

@Database(
    entities = [
        LessonIdEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun favoriteLessonDao(): FavoriteLessonDao
}