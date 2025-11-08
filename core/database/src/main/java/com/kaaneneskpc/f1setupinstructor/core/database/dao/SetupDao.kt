package com.kaaneneskpc.f1setupinstructor.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaaneneskpc.f1setupinstructor.core.database.entity.SetupEntity

@Dao
interface SetupDao {
    
    @Query("""
        SELECT * FROM setups 
        WHERE circuit = :circuit 
        AND weatherQuali = :qualiWeather 
        AND weatherRace = :raceWeather 
        ORDER BY score DESC, sourcePublishedAt DESC
    """)
    fun getSetups(
        circuit: String,
        qualiWeather: String,
        raceWeather: String
    ): PagingSource<Int, SetupEntity>
    
    @Query("SELECT * FROM setups WHERE sourceUrl = :sourceUrl")
    suspend fun getSetupBySourceUrl(sourceUrl: String): SetupEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setup: SetupEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(setups: List<SetupEntity>)
    
    @Query("DELETE FROM setups WHERE circuit = :circuit")
    suspend fun deleteByCircuit(circuit: String)
    
    @Query("DELETE FROM setups")
    suspend fun deleteAll()
}
