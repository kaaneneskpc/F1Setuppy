package com.kaaneneskpc.f1setupinstructor.core.database.di

import android.content.Context
import androidx.room.Room
import com.kaaneneskpc.f1setupinstructor.core.database.AppDatabase
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
            "f1-setup-instructor-db"
        ).build()
    }

    @Provides
    fun provideSetupDao(appDatabase: AppDatabase): SetupDao {
        return appDatabase.setupDao()
    }

    @Provides
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.historyDao()
    }
}
