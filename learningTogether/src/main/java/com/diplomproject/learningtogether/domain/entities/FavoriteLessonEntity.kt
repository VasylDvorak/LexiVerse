package com.diplomproject.learningtogether.domain.entities

/**
 * по сути мы переопределили существующую сущьность LessonEntity. При этом мы
 * рассширили ее дополнительными полями.
 * Можно отнаследоватся от существующей сущьности (LessonEntity), но в этом
 * случае в data class перестанет работать внутреняя логика (внутреняя анотация)
 */
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite")
data class FavoriteLessonEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Long = 0,

    val name: String = "",

    val imageUrl: String = "",

    val victoryImageUrl: String? = null,

    val tasks: MutableList<TaskEntity> = mutableListOf(),

    var isFavorite: Boolean = false,

    var courseId: Long = 0

) : Parcelable
