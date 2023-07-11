package com.diplomproject.room.history

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntityTable")
    suspend fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntityTable WHERE word LIKE :word")
    suspend fun getDataByWord(word: String): HistoryEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<HistoryEntity>)

    @Update
    suspend fun update(entity: HistoryEntity)

    @Delete
    suspend fun delete(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntityTable WHERE word LIKE :word")
    suspend fun deleteItem(word: String)


}
