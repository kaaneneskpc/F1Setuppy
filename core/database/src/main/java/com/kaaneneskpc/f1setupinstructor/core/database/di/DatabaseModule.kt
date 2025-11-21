package com.kaaneneskpc.f1setupinstructor.core.database.di

import android.content.Context
import androidx.room.Room
import com.kaaneneskpc.f1setupinstructor.core.database.AppDatabase
import com.kaaneneskpc.f1setupinstructor.core.database.F1SetupDatabase
import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
        .fallbackToDestructiveMigration(true)
        .build()
    }

    @Provides
    @Singleton
    fun provideF1SetupDatabase(@ApplicationContext context: Context): F1SetupDatabase {
        return Room.databaseBuilder(
            context,
            F1SetupDatabase::class.java,
            "f1_setup_db"
        )
        .fallbackToDestructiveMigration(true)
        .build()
    }

    @Provides
    @Singleton
    fun provideSetupDao(database: AppDatabase): SetupDao {
        return database.setupDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: F1SetupDatabase): HistoryDao {
        return database.historyDao()
    }
}
