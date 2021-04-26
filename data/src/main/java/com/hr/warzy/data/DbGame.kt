package com.hr.warzy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "games")
data class DbGame(@PrimaryKey(autoGenerate = true) val id: Long = 0, val timestamp: Calendar = Calendar.getInstance())

class CalendarTypeConverter {

    @TypeConverter
    fun fromCalendar(cal: Calendar): Long = cal.timeInMillis

    @TypeConverter
    fun toCalendar(millis: Long): Calendar = Calendar.getInstance().apply { timeInMillis = millis }
}