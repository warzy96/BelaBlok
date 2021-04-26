package com.hr.warzy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface GameDao {

    @Query("SELECT * FROM games")
    fun getAllGames(): Flowable<List<DbGame>>

    @Insert
    fun insertGame(dbGame: DbGame): Long

    @Delete
    fun removeGame(dbGame: DbGame): Completable

    @Query("DELETE FROM games WHERE id == :gameId")
    fun removeGame(gameId: Long): Completable
}