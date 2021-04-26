package com.hr.warzy.bela.view

enum class PlayedGamesListItemToCurrentGame(val transitionName: String = "PlayedGamesListItemToCurrentGame") {
    PLAYED_GAMES_LIST_ITEM,
    CURRENT_GAME
}

enum class CurrentGameToNewTurn(val transitionName: String = "CurrentGameToNewTurn") {
    NEW_TURN,
    CURRENT_GAME
}