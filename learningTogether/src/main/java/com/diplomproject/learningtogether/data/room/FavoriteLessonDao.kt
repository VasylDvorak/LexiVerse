package com.diplomproject.learningtogether.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity

@Dao
interface FavoriteLessonDao {

    @Query("SELECT * FROM lesson_id")
    fun getAllFavorites(): List<LessonIdEntity>

    @Insert
    fun addFavorite(lessonId: LessonIdEntity)
//
//    @Update
//    fun updateFavorite(lessonId: LessonIdEntity)

    @Delete
    fun deleteFavorite(lessonId: LessonIdEntity)
}