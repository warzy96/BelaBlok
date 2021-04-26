package com.hr.warzy.data

import com.hr.warzy.domain.Game
import com.hr.warzy.domain.GamesRepository
import com.hr.warzy.domain.Turn
import com.hr.warzy.domain.interactor.CalculateTotalScoreUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class GamesRepositoryImpl(
    private val gameDao: GameDao,
    private val turnDao: TurnDao,
    private val calculateTotalScoreUseCase: CalculateTotalScoreUseCase
) : GamesRepository {

    override fun createGame(): Long = gameDao.insertGame(DbGame())

    override fun createTurn(
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
    ): Completable = turnDao.insertTurn(
        DbTurn(
            ourScore = ourScore,
            yourScore = yourScore,
            gameId = gameId,
            ourFifties = outFifties,
            ourBelot = ourBelot,
            ourTwenties = ourTwenties,
            ourFourJacks = ourFourJacks,
            ourFourNines = ourFourNines,
            ourHundreds = ourHundreds,
            yourBelot = yourBelot,
            yourFifties = yourFifties,
            yourFourJacks = yourFourJacks,
            yourFourNines = yourFourNines,
            yourHundreds = yourHundreds,
            yourTwenties = yourTwenties
        )
    )

    override fun getAllGames(): Flowable<List<Game>> {
        return Flowable.combineLatest(
            gameDao.getAllGames(),
            turnDao.getAllTurns(),
            BiFunction { dbGames, turns ->
                val games = mutableListOf<Game>()
                dbGames.forEach { game ->
                    var ourCurrentTotalScore = 0
                    var yourCurrentTotalScore = 0

                    var numberOfOurWins = 0
                    var numberOfYourWins = 0

                    val turnsForGame = turns.filter { it.gameId == game.id }
                    turnsForGame.forEach {
                        ourCurrentTotalScore += calculateTotalScoreUseCase.calculate(
                            it.ourScore,
                            it.ourTwenties,
                            it.ourFifties,
                            it.ourHundreds,
                            it.ourBelot,
                            it.ourFourJacks,
                            it.ourFourNines
                        )
                        yourCurrentTotalScore += calculateTotalScoreUseCase.calculate(
                            it.yourScore,
                            it.yourTwenties,
                            it.yourFifties,
                            it.yourHundreds,
                            it.yourBelot,
                            it.yourFourJacks,
                            it.yourFourNines
                        )

                        if (ourCurrentTotalScore > 1001 || yourCurrentTotalScore > 1001) {
                            if (ourCurrentTotalScore > 1001 && yourCurrentTotalScore > 1001) {
                                if (ourCurrentTotalScore >= yourCurrentTotalScore) numberOfOurWins++
                                else numberOfYourWins++
                            } else {
                                if (ourCurrentTotalScore > 1001) numberOfOurWins++
                                if (yourCurrentTotalScore > 1001) numberOfYourWins++
                            }

                            ourCurrentTotalScore = 0
                            yourCurrentTotalScore = 0
                        }
                    }
                    games.add(
                        Game(
                            game.id,
                            numberOfOurWins,
                            numberOfYourWins,
                            game.timestamp
                        )
                    )
                }
                games
            }
        )
    }

    override fun getAllTurnsForGame(gameId: Long): Flowable<List<Turn>> =
        turnDao.getTurnsForGame(gameId).map { dbTurns ->
            val turns = mutableListOf<Turn>()

            dbTurns.forEach {
                turns.add(
                    Turn(
                        it.id,
                        it.gameId,
                        it.ourScore,
                        it.yourScore,
                        it.ourFourJacks,
                        it.ourFourNines,
                        it.ourBelot,
                        it.ourTwenties,
                        it.ourFifties,
                        it.ourHundreds,
                        it.yourFourJacks,
                        it.yourFourNines,
                        it.yourBelot,
                        it.yourTwenties,
                        it.yourFifties,
                        it.yourHundreds
                    )
                )
            }

            turns
        }

    override fun removeGame(gameId: Long): Completable = gameDao.removeGame(gameId)

    override fun getTurn(turnId: Long): Flowable<Turn> =
        turnDao.getTurn(turnId).map {
            Turn(
                it.id,
                it.gameId,
                it.ourScore,
                it.yourScore,
                it.ourFourJacks,
                it.ourFourNines,
                it.ourBelot,
                it.ourTwenties,
                it.ourFifties,
                it.ourHundreds,
                it.yourFourJacks,
                it.yourFourNines,
                it.yourBelot,
                it.yourTwenties,
                it.yourFifties,
                it.yourHundreds
            )
        }

    override fun updateTurn(turn: Turn) =
        with(turn) {
            turnDao.updateTurn(
                DbTurn(
                    id = id,
                    ourScore = ourScore,
                    yourScore = yourScore,
                    ourFourJacks = ourFourJacks,
                    yourFourJacks = yourFourJacks,
                    ourTwenties = ourTwenties,
                    yourTwenties = yourTwenties,
                    ourBelot = ourBelot,
                    yourBelot = yourBelot,
                    ourFifties = ourFifties,
                    yourFifties = yourFifties,
                    ourFourNines = ourFourNines,
                    yourHundreds = yourHundreds,
                    ourHundreds = ourHundreds,
                    yourFourNines = yourFourNines,
                    gameId = gameId
                )
            )
        }
}