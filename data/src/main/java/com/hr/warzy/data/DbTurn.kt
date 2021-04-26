package com.hr.warzy.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "turns",
    foreignKeys = [ForeignKey(
        entity = DbGame::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("gameId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["gameId"])]
)

data class DbTurn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    val yourHundreds: Int,
    val gameId: Long
)