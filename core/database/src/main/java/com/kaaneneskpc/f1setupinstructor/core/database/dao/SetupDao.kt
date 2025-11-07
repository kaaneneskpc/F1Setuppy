package com.kaaneneskpc.f1setupinstructor.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SetupEntity

@Dao
interface SetupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(setups: List<SetupEntity>)

    @Query("SELECT * FROM setups WHERE circuit = :circuit AND weatherQuali = :weatherQuali AND weatherRace = :weatherRace")
    fun getSetups(circuit: String, weatherQuali: String, weatherRace: String): PagingSource<Int, SetupEntity>

    @Query("SELECT * FROM setups WHERE sourceUrl = :sourceUrl")
    suspend fun getSetupBySourceUrl(sourceUrl: String): SetupEntity?

    @Query("DELETE FROM setups")
    suspend fun clearSetups()
}
