package com.hr.warzy.bela.playedgameslist

import android.view.View
import com.hr.warzy.bela.base.BaseViewModel
import com.hr.warzy.bela.navigation.RoutingActionsDispatcher
import com.hr.warzy.bela.util.toTimestampText
import com.hr.warzy.domain.GamesRepository
import io.reactivex.Scheduler
import io.reactivex.processors.BehaviorProcessor

class PlayedGamesListViewModel(
    private val gamesRepository: GamesRepository,
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) :
    BaseViewModel<PlayedGamesListViewState>(
        mainThreadScheduler = mainThreadScheduler,
        backgroundScheduler = backgroundScheduler,
        routingActionsDispatcher = routingActionsDispatcher
    ) {

    private val numberOfPlayedGamesPublisher = BehaviorProcessor.create<Int>()

    fun openNewGameScreen(sharedView: View) = dispatchRoutingAction { it.openNewGameScreen(sharedView) }

    fun openCurrentGameScreen(gameId: Long) = dispatchRoutingAction { it.openCurrentGameScreen(gameId) }

    fun removeGame(gameId: Long) = runCommand(gamesRepository.removeGame(gameId))

    fun openSettings() = dispatchRoutingAction { it.openSettings() }

    init {
        query(
            gamesRepository
                .getAllGames()
                .map { list ->
                    val items = mutableListOf<PlayedGameItems>()

                    val sortedList = list.sortedByDescending { it.timestamp.timeInMillis }
                    sortedList.forEachIndexed { index, game ->
                        val previous = sortedList.getOrNull(index - 1)

                        if (previous?.let { toTimestampText(it.timestamp.time) } != toTimestampText(game.timestamp.time)) {
                            items.add(PlayedGameItems.TimeStamp(toTimestampText(game.timestamp.time)))
                        }

                        with(game) {
                            items.add(
                                PlayedGameItems.ItemPlayedGame(
                                    id,
                                    ourTotalScore,
                                    yourTotalScore
                                )
                            )
                        }
                    }
                    numberOfPlayedGamesPublisher.onNext(items.size)
                    PlayedGamesListViewState.PlayedGames(items)
                }
        )

        query(
            numberOfPlayedGamesPublisher.observeOn(backgroundScheduler)
                .scan(Pair(0, 0)) { previousValue, nextValue -> Pair(previousValue.second, nextValue) }
                .map { PlayedGamesListViewState.ShouldScrollToTop(it.second > it.first) }
        )
    }
}