package com.hr.warzy.data

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TurnDao {

    @Insert
    fun insertTurn(dbTurn: DbTurn): Completable

    @Delete
    fun deleteTurn(dbTurn: DbTurn): Completable

    @Query("SELECT * FROM turns WHERE :gameId == gameId")
    fun getTurnsForGame(gameId: Long): Flowable<List<DbTurn>>

    @Query("SELECT * FROM turns")
    fun getAllTurns(): Flowable<List<DbTurn>>

    @Update
    fun updateTurn(dbTurn: DbTurn): Completable

    @Query("SELECT * FROM turns WHERE :turnId == id")
    fun getTurn(turnId: Long): Flowable<DbTurn>
}