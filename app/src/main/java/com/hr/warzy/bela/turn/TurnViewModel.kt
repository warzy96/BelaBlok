package com.hr.warzy.bela.turn

import com.hr.warzy.bela.base.BaseViewModel
import com.hr.warzy.bela.navigation.RoutingActionsDispatcher
import com.hr.warzy.domain.GamesRepository
import com.hr.warzy.domain.Turn
import com.hr.warzy.domain.interactor.CalculateTotalScoreUseCase
import io.reactivex.Scheduler
import io.reactivex.processors.BehaviorProcessor

private const val DEFAULT_TURN_ID = -1L

class TurnViewModel(
    turnId: Long = DEFAULT_TURN_ID,
    private val gameId: Long,
    private val gamesRepository: GamesRepository,
    calculateTotalScoreUseCase: CalculateTotalScoreUseCase,
    private val mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<TurnViewState>(
    mainThreadScheduler = mainThreadScheduler,
    backgroundScheduler = backgroundScheduler,
    routingActionsDispatcher = routingActionsDispatcher
) {

    private val ourScorePublisher: BehaviorProcessor<Int> = BehaviorProcessor.create()
    private val yourScorePublisher: BehaviorProcessor<Int> = BehaviorProcessor.create()
    private val totalScorePublisher: BehaviorProcessor<TurnData> = BehaviorProcessor.create()

    private var currentTurn: Turn? = null

    init {
        query(
            gamesRepository.getTurn(turnId).map {
                currentTurn = it
                TurnViewState.Score(
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
        )

        query(
            ourScorePublisher.observeOn(backgroundScheduler)
                .distinctUntilChanged()
                .map { TurnViewState.OnOurScoreChanged(toOppositeTeamScore(it)) }
        )

        query(
            yourScorePublisher.observeOn(backgroundScheduler)
                .distinctUntilChanged()
                .map { TurnViewState.OnYourScoreChanged(toOppositeTeamScore(it)) }
        )

        query(
            totalScorePublisher.observeOn(backgroundScheduler)
                .map {
                    with(it) {
                        TurnViewState.TotalScoreHeader(
                            ourTotalScore = calculateTotalScoreUseCase.calculate(
                                score = ourScore,
                                belot = ourBelot,
                                hundreds = ourHundreds,
                                fourNines = ourFourNines,
                                fourJacks = ourFourJacks,
                                twenties = ourTwenties,
                                fifties = ourFifties
                            ),
                            yourTotalScore = calculateTotalScoreUseCase.calculate(
                                score = yourScore,
                                belot = yourBelot,
                                hundreds = yourHundreds,
                                fourNines = yourFourNines,
                                fourJacks = yourFourJacks,
                                twenties = yourTwenties,
                                fifties = yourFifties
                            )
                        )
                    }
                }
        )
    }

    private fun toOppositeTeamScore(score: Int) = if (score >= 162) 0 else 162 - score

    fun saveTurn(turnData: TurnData) =
        with(turnData) {
            if (currentTurn != null) runCommand(gamesRepository.updateTurn(
                Turn(
                    gameId = gameId,
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
                    id = currentTurn!!.id
                )

            )
                .observeOn(mainThreadScheduler)
                .doOnComplete { goBack() }
            )
            else runCommand(
                gamesRepository.createTurn(
                    ourScore,
                    yourScore,
                    gameId,
                    ourFourJacks,
                    ourFourNines,
                    ourBelot,
                    ourTwenties,
                    ourFifties,
                    ourHundreds,
                    yourFourJacks,
                    yourFourNines,
                    yourBelot,
                    yourTwenties,
                    yourFifties,
                    yourHundreds
                )
                    .observeOn(mainThreadScheduler)
                    .doOnComplete { goBack() }
            )
        }

    fun goBack() = dispatchRoutingAction { it.goBack() }

    fun ourScoreTextChanged(score: String) = ourScorePublisher.onNext(score.toIntOrNull() ?: 0)

    fun yourScoreChanged(score: String) = yourScorePublisher.onNext(score.toIntOrNull() ?: 0)

    fun updateTotalScore(turnData: TurnData) = totalScorePublisher.onNext(turnData)
}

data class TurnData(
    val ourScore: Int,
    val yourScore: Int,
    val ourFourJacks: Boolean,
    val ourFourNines: Boolean,
    val ourBelot: Boolean,
    val ourTwenties: Int,
    val ourFifties: Int,
    val ourHundreds: Int,
    val yourFourJacks: Boolean,
    val yourFourNines: Boolean,
    val yourBelot: Boolean,
    val yourTwenties: Int,
    val yourFifties: Int,
    val yourHundreds: Int
)