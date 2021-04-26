package com.hr.warzy.domain

import io.reactivex.Completable
import io.reactivex.Flowable

interface GamesRepository {

    fun createGame(): Long

    fun createTurn(
        ourScore: Int,
        yourScore: Int,
        gameId: Long,
        ourFourJacks: Boolean,
        ourFourNines: Boolean,
        ourBelot: Boolean,
        ourTwenties: Int,
        outFifties: Int,
        ourHundreds: Int,
        yourFourJacks: Boolean,
        yourFourNines: Boolean,
        yourBelot: Boolean,
        yourTwenties: Int,
        yourFifties: Int,
        yourHundreds: Int
    ): Completable

    fun getAllGames(): Flowable<List<Game>>

    fun getAllTurnsForGame(gameId: Long): Flowable<List<Turn>>

    fun removeGame(gameId: Long): Completable

    fun getTurn(turnId: Long): Flowable<Turn>

    fun updateTurn(turn: Turn): Completable
}