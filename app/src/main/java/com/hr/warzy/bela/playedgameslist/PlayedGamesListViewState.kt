package com.hr.warzy.bela.playedgameslist

sealed class PlayedGamesListViewState {
    data class PlayedGames(val playedGamesList: List<PlayedGameItems>): PlayedGamesListViewState()
    data class ShouldScrollToTop(val shouldScrollToTop: Boolean): PlayedGamesListViewState()
}