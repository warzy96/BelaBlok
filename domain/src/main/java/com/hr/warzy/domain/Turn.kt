package com.hr.warzy.domain

data class Turn(
    val id: Long,
    val gameId: Long,
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