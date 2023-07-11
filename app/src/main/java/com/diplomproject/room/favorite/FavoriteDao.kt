package com.diplomproject.room.favorite

import androidx.room.*

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM FavoriteEntityTable")
    suspend fun all(): List<FavoriteEntity>

    @Query("SELECT * FROM FavoriteEntityTable WHERE word LIKE :word")
    suspend fun getDataByWord(word: String): FavoriteEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<FavoriteEntity>)

    @Update
    suspend fun update(entity: FavoriteEntity)

    @Delete
    suspend fun delete(entity: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntityTable WHERE word LIKE :word")
    suspend fun deleteItem(word: String)
}
