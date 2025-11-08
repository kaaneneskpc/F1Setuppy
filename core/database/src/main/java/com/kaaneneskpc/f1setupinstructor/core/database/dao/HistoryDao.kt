package com.kaaneneskpc.f1setupinstructor.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getHistories(): Flow<List<HistoryItemEntity>>

    @Query("SELECT * FROM history WHERE timestamp = :timestamp")
    suspend fun getHistoryByTimestamp(timestamp: Long): HistoryItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryItemEntity)

    @Delete
    suspend fun deleteHistory(history: HistoryItemEntity)
}
