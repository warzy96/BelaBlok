package com.hr.warzy.bela.currentgame

import android.view.View
import com.hr.warzy.bela.base.BaseViewModel
import com.hr.warzy.bela.navigation.RoutingActionsDispatcher
import com.hr.warzy.domain.GamesRepository
import com.hr.warzy.domain.interactor.CalculateTotalScoreUseCase
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor

private const val DEFAULT_GAME_ID = -1L

class CurrentGameViewModel(
    gameId: Long = DEFAULT_GAME_ID,
    repository: GamesRepository,
    calculateTotalScoreUseCase: CalculateTotalScoreUseCase,
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<CurrentGameViewState>(
    mainThreadScheduler = mainThreadScheduler,
    backgroundScheduler = backgroundScheduler,
    routingActionsDispatcher = routingActionsDispatcher
) {

    private val gameIdPublisher = BehaviorProcessor.createDefault(gameId)
    private val offsetChangePublisher = PublishProcessor.create<Float>()

    init {
        if (gameId == DEFAULT_GAME_ID) runCommand(
            Completable.fromAction {
                gameIdPublisher.onNext(repository.createGame()) // Create a new game if this one does not exist
            })

        query(offsetChangePublisher.observeOn(backgroundScheduler).map(CurrentGameViewState::AnimationProgress))

        query(gameIdPublisher.observeOn(backgroundScheduler)
            .switchMap {
                repository.getAllTurnsForGame(it)
                    .map {
                        var ourCurrentTotalScore = 0
                        var yourCurrentTotalScore = 0

                        var numberOfOurWins = 0
                        var numberOfYourWins = 0

                        val result = mutableListOf<CurrentGameTurnItems>()
                        it.forEachIndexed { index, turn ->
                            val ourCurrentScore = calculateTotalScoreUseCase.calculate(
                                turn.ourScore,
                                turn.ourTwenties,
                                turn.ourFifties,
                                turn.ourHundreds,
                                turn.ourBelot,
                                turn.ourFourJacks,
                                turn.ourFourNines
                            )
                            val yourCurrentScore = calculateTotalScoreUseCase.calculate(
                                turn.yourScore,
                                turn.yourTwenties,
                                turn.yourFifties,
                                turn.yourHundreds,
                                turn.yourBelot,
                                turn.yourFourJacks,
                                turn.yourFourNines
                            )
                            ourCurrentTotalScore += ourCurrentScore
                            yourCurrentTotalScore += yourCurrentScore

                            result.add(
                                CurrentGameTurnItems.TurnItem(
                                    turn.id,
                                    ourCurrentScore,
                                    yourCurrentScore,
                                    turn.ourFourJacks,
                                    turn.ourFourNines,
                                    turn.ourBelot,
                                    turn.ourTwenties,
                                    turn.ourFifties,
                                    turn.ourHundreds,
                                    turn.yourFourJacks,
                                    turn.yourFourNines,
                                    turn.yourBelot,
                                    turn.yourTwenties,
                                    turn.yourFifties,
                                    turn.yourHundreds
                                )
                            )

                            if (ourCurrentTotalScore > 1001 || yourCurrentTotalScore > 1001) {
                                if (ourCurrentTotalScore > 1001 && yourCurrentTotalScore > 1001) {
                                    if (ourCurrentTotalScore >= yourCurrentTotalScore) numberOfOurWins++
                                    else numberOfYourWins++
                                } else {
                                    if (ourCurrentTotalScore > 1001) numberOfOurWins++
                                    if (yourCurrentTotalScore > 1001) numberOfYourWins++
                                }

                                result.add(
                                    CurrentGameTurnItems.EndOfTurnItem(gameId + index, numberOfOurWins, numberOfYourWins)
                                )

                                ourCurrentTotalScore = 0
                                yourCurrentTotalScore = 0
                            }
                        }

                        CurrentGameViewState.Turns(result.reversed())
                    }
            })

        query(
            gameIdPublisher.observeOn(backgroundScheduler)
                .switchMap {
                    repository.getAllTurnsForGame(it)
                        .map {
                            var ourCurrentTotalScore = 0
                            var yourCurrentTotalScore = 0

                            it.forEach { turn ->
                                val ourCurrentScore = calculateTotalScoreUseCase.calculate(
                                    turn.ourScore,
                                    turn.ourTwenties,
                                    turn.ourFifties,
                                    turn.ourHundreds,
                                    turn.ourBelot,
                                    turn.ourFourJacks,
                                    turn.ourFourNines
                                )
                                val yourCurrentScore = calculateTotalScoreUseCase.calculate(
                                    turn.yourScore,
                                    turn.yourTwenties,
                                    turn.yourFifties,
                                    turn.yourHundreds,
                                    turn.yourBelot,
                                    turn.yourFourJacks,
                                    turn.yourFourNines
                                )
                                ourCurrentTotalScore += ourCurrentScore
                                yourCurrentTotalScore += yourCurrentScore

                                if (ourCurrentTotalScore > 1001 || yourCurrentTotalScore > 1001) {
                                    ourCurrentTotalScore = 0
                                    yourCurrentTotalScore = 0
                                }
                            }

                            CurrentGameViewState.Header(ourCurrentTotalScore, yourCurrentTotalScore)
                        }
                }
        )
    }

    fun openNewTurnScreen(sharedView: View) = dispatchRoutingAction { it.openNewTurnScreen(gameIdPublisher.value!!, sharedView) }

    fun openExistingTurnScreen(turnId: Long) = dispatchRoutingAction { it.openExistingTurnScreen(gameIdPublisher.value!!, turnId) }

    fun goBack() = dispatchRoutingAction { it.goBack() }

    fun appBarOffsetChange(progressRatio: Float) = offsetChangePublisher.onNext(progressRatio)
}