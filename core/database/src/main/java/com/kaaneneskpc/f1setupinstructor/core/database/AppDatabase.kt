package com.kaaneneskpc.f1setupinstructor.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao
import com.kaaneneskpc.f1setupinstructor.core.database.entity.HistoryItemEntity
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SetupEntity

@Database(entities = [SetupEntity::class, HistoryItemEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setupDao(): SetupDao
    abstract fun historyDao(): HistoryDao
}
