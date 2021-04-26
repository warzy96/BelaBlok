package com.hr.warzy.domain.interactor

class CalculateTotalScoreUseCase {

    fun calculate(score: Int, twenties: Int, fifties: Int, hundreds: Int, belot: Boolean, fourJacks: Boolean, fourNines: Boolean) =
        score +
                twenties * 20 +
                fifties * 50 +
                hundreds * 100 +
                (if (belot) 1001 else 0) +
                (if (fourJacks) 200 else 0) +
                (if (fourNines) 150 else 0)
}