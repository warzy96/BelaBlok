package com.hr.warzy.bela.currentgame

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.hr.warzy.bela.R
import com.hr.warzy.bela.base.BaseFragment
import com.hr.warzy.bela.view.ItemSpaceDecoration
import com.hr.warzy.bela.view.scrollToStart
import kotlinx.android.synthetic.main.fragment_current_game.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.math.absoluteValue

private const val GAME_ID_KEY = "game_id"

class CurrentGameFragment : BaseFragment<CurrentGameViewState>(R.layout.fragment_current_game) {

    companion object {
        const val TAG = "CurrentGameFragment"

        fun newInstance(gameId: Long) = CurrentGameFragment().apply {
            arguments = Bundle().apply {
                putLong(GAME_ID_KEY, gameId)
            }
        }
    }

    private val gameId: Long by lazy { arguments?.getLong(GAME_ID_KEY) ?: -1 }

    private lateinit var adapter: CurrentGameTurnAdapter

    override val model: CurrentGameViewModel by inject { parametersOf(gameId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()
    }

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        with(current_game_turn_list) {
            this@CurrentGameFragment.adapter = CurrentGameTurnAdapter(layoutInflater) { model.openExistingTurnScreen(it) }
            adapter = this@CurrentGameFragment.adapter
            addItemDecoration(ItemSpaceDecoration(resources.getDimensionPixelSize(R.dimen.item_spacing)))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) current_game_new_turn.hide()
                    else current_game_new_turn.show()
                }
            })
        }

        current_game_app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            model.appBarOffsetChange(verticalOffset.toFloat().absoluteValue / appBarLayout.totalScrollRange.toFloat())
        })
        current_game_new_turn.setOnClickListener { model.openNewTurnScreen(current_game_new_turn) }
        current_game_toolbar.setNavigationOnClickListener { model.goBack() }
    }

    override fun render(viewState: CurrentGameViewState) =
        when (viewState) {
            is CurrentGameViewState.Turns -> {
                adapter.submitList(viewState.turns)
                current_game_turn_list.scrollToStart()
            }
            is CurrentGameViewState.Header -> renderHeader(viewState)
            is CurrentGameViewState.AnimationProgress -> {
                current_game_header_layout.progress = viewState.progress
            }
        }

    private fun renderHeader(viewState: CurrentGameViewState.Header) {
        current_game_mi_score.text = viewState.ourScore.toString()
        current_game_vi_score.text = viewState.yourScore.toString()
    }
}