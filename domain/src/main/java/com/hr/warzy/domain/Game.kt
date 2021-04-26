package com.hr.warzy.domain

import java.util.*

data class Game(val id: Long, val ourTotalScore: Int, val yourTotalScore: Int, val timestamp: Calendar)