package com.hr.warzy.bela.turn

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.transition.MaterialContainerTransform
import com.hr.warzy.bela.R
import com.hr.warzy.bela.base.BaseFragment
import com.hr.warzy.bela.view.CurrentGameToNewTurn
import kotlinx.android.synthetic.main.fragment_turn.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

private const val GAME_ID_KEY = "game_id_key"
private const val TURN_ID_KEY = "turn_id_key"

class TurnFragment : BaseFragment<TurnViewState>(R.layout.fragment_turn) {

    companion object {
        const val TAG = "TurnFragment"

        fun newInstance(gameId: Long, turnId: Long = -1) = TurnFragment().apply {
            arguments = Bundle().apply {
                putLong(GAME_ID_KEY, gameId)
                putLong(TURN_ID_KEY, turnId)
            }
        }
    }

    private val gameId: Long by lazy { requireArguments().getLong(GAME_ID_KEY) }
    private val turnId: Long by lazy { requireArguments().getLong(TURN_ID_KEY, -1) }

    override val model: TurnViewModel by inject { parametersOf(turnId, gameId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        turn_toolbar.setNavigationOnClickListener { model.goBack() }
        turn_toolbar.setOnMenuItemClickListener {
            it?.let {
                when (it.itemId) {
                    R.id.turn_menu_save -> {
                        model.saveTurn(gatherTurnData())
                        true
                    }
                    else -> false
                }
            } ?: false
        }

        turn_mi_score.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { model.ourScoreTextChanged(s.toString()) }
                model.updateTotalScore(gatherTurnData())
            }
        })

        turn_vi_score.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { model.yourScoreChanged(s.toString()) }
                model.updateTotalScore(gatherTurnData())
            }
        })

        turn_is_two_hundred_mi.setOnCheckedChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_is_two_hundred_vi.setOnCheckedChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_is_hundred_fifty_mi.setOnCheckedChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_is_hundred_fifty_vi.setOnCheckedChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_fifties_mi.setOnValueChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_fifties_vi.setOnValueChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_hundreds_mi.setOnValueChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_hundreds_vi.setOnValueChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_twenties_mi.setOnValueChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_twenties_vi.setOnValueChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_is_belot_mi.setOnCheckedChangedListener { model.updateTotalScore(gatherTurnData()) }
        turn_is_belot_vi.setOnCheckedChangedListener { model.updateTotalScore(gatherTurnData()) }
    }

    override fun render(viewState: TurnViewState) =
        when (viewState) {
            is TurnViewState.Score -> renderTurn(viewState)
            is TurnViewState.OnOurScoreChanged -> if (turn_vi_score.text.toString() != viewState.yourScore.toString()) turn_vi_score.setText(viewState.yourScore.toString()) else Unit
            is TurnViewState.OnYourScoreChanged -> if (turn_mi_score.text.toString() != viewState.ourScore.toString()) turn_mi_score.setText(viewState.ourScore.toString()) else Unit
            is TurnViewState.TotalScoreHeader -> renderHeader(viewState)
        }

    private fun renderHeader(viewState: TurnViewState.TotalScoreHeader) {
        turn_total_score_mi.text = viewState.ourTotalScore.toString()
        turn_total_score_vi.text = viewState.yourTotalScore.toString()
    }

    private fun renderTurn(viewState: TurnViewState.Score) =
        with(viewState) {
            turn_mi_score.setText(ourScore.toString())
            turn_vi_score.setText(yourScore.toString())
            turn_is_two_hundred_mi.isChecked = ourFourJacks
            turn_is_hundred_fifty_mi.isChecked = ourFourNines
            turn_is_belot_mi.isChecked = ourBelot
            turn_twenties_mi.currentValue = ourTwenties
            turn_fifties_mi.currentValue = ourFifties
            turn_hundreds_mi.currentValue = ourHundreds
            turn_is_two_hundred_vi.isChecked = yourFourJacks
            turn_is_hundred_fifty_vi.isChecked = yourFourNines
            turn_is_belot_vi.isChecked = yourBelot
            turn_twenties_vi.currentValue = yourTwenties
            turn_fifties_vi.currentValue = yourFifties
            turn_hundreds_vi.currentValue = yourHundreds
        }

    private fun gatherTurnData(): TurnData =
        TurnData(
            turn_mi_score.text.toString().toIntOrNull() ?: 0,
            turn_vi_score.text.toString().toIntOrNull() ?: 0,
            ourFourJacks = turn_is_two_hundred_mi.isChecked,
            ourFourNines = turn_is_hundred_fifty_mi.isChecked,
            ourBelot = turn_is_belot_mi.isChecked,
            ourTwenties = turn_twenties_mi.currentValue,
            ourFifties = turn_fifties_mi.currentValue,
            ourHundreds = turn_hundreds_mi.currentValue,
            yourFourJacks = turn_is_two_hundred_vi.isChecked,
            yourFourNines = turn_is_hundred_fifty_vi.isChecked,
            yourBelot = turn_is_belot_vi.isChecked,
            yourTwenties = turn_twenties_vi.currentValue,
            yourFifties = turn_fifties_vi.currentValue,
            yourHundreds = turn_hundreds_vi.currentValue
        )
}

