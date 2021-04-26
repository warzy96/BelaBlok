package com.hr.warzy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DbGame::class, DbTurn::class], version = 4)
@TypeConverters(CalendarTypeConverter::class)
abstract class GamesDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun turnDao(): TurnDao
}