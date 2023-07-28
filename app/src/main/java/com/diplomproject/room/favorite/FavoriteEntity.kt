package com.diplomproject.room.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "FavoriteEntityTable",
    indices = arrayOf(Index(value = arrayOf("word"), unique = true))
)
data class FavoriteEntity(
    @field:ColumnInfo(name = "id") var id: Int?,
    @field:PrimaryKey
    @field:ColumnInfo(name = "word") var word: String,
    @field:ColumnInfo(name = "imageUrl") var imageUrl: String?,
    @field:ColumnInfo(name = "transcription") var transcription: String?,
    @field:ColumnInfo(name = "soundUrl") var soundUrl: String?,
    @field:ColumnInfo(name = "translation") var translation: String?,
    @field:ColumnInfo(name = "examples") var examples: String? ="[]"
)

