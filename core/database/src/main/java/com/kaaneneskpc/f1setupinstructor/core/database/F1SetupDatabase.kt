package com.kaaneneskpc.f1setupinstructor.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao
import com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity

@Database(
    entities = [HistoryItemEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class F1SetupDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
