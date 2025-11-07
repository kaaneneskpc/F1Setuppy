package com.kaaneneskpc.f1setupinstructor.core.database

import androidx.room.TypeConverter
import com.kaaneneskpc.f1setupinstructor.domain.model.SetupStyle
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    @TypeConverter
    fun fromSetupStyle(value: String?): SetupStyle? {
        return value?.let { SetupStyle.valueOf(it) }
    }

    @TypeConverter
    fun setupStyleToString(setupStyle: SetupStyle?): String? {
        return setupStyle?.name
    }
}
