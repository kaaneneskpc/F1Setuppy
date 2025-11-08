package com.kaaneneskpc.f1setupinstructor.core.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import java.time.Instant

/**
 * Room TypeConverters for complex data types
 * Required for Instant and SetupStyle enum
 */
@RequiresApi(Build.VERSION_CODES.O)
class Converters {
    
    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilli()
    }
    
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }
    
    @TypeConverter
    fun fromSetupStyle(value: SetupStyle?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toSetupStyle(value: String?): SetupStyle? {
        return value?.let { SetupStyle.valueOf(it) }
    }
}
