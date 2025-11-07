package com.kaaneneskpc.f1setupinstructor.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyItem: HistoryItemEntity)

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<HistoryItemEntity>>

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}
