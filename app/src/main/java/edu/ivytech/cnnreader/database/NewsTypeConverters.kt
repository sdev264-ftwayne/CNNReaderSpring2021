package edu.ivytech.cnnreader.database

import androidx.room.TypeConverter
import java.util.*

class NewsTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid:String?): UUID?{
        return UUID.fromString(uuid)
    }
}