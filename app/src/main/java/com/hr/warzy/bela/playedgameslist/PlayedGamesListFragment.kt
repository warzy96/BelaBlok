package com.hr.warzy.bela.playedgameslist

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.hr.warzy.bela.R
import com.hr.warzy.bela.base.BaseFragment
import com.hr.warzy.bela.view.ItemSpaceDecoration
import com.hr.warzy.bela.view.SwipeItemCallback
import com.hr.warzy.bela.view.scrollToStart
import kotlinx.android.synthetic.main.fragment_played_games_list.*
import org.koin.android.ext.android.inject

class PlayedGamesListFragment : BaseFragment<PlayedGamesListViewState>(R.layout.fragment_played_games_list) {

    companion object {
        const val TAG = "PlayedGamesListFragment"
    }

    override val model: PlayedGamesListViewModel by inject()

    private lateinit var playedGamesListAdapter: PlayedGamesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Hold()
    }

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        with(player_games_list_recycler_view) {
            playedGamesListAdapter = PlayedGamesListAdapter(layoutInflater, { model.removeGame(it) }) { gameId ->
                model.openCurrentGameScreen(gameId)
            }
            adapter = playedGamesListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(ItemSpaceDecoration(resources.getDimensionPixelSize(R.dimen.item_spacing)))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) fragment_played_games_list_new_game?.hide()
                    else fragment_played_games_list_new_game?.show()
                }
            })
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeItemCallback(
                playedGamesListAdapter,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_delete_24, requireContext().theme)!!,
                ContextCompat.getDrawable(requireContext(), R.drawable.item_swiped_delete_background)!!
            )
        )
        itemTouchHelper.attachToRecyclerView(player_games_list_recycler_view)

        fragment_played_games_list_new_game.setOnClickListener { model.openNewGameScreen(fragment_played_games_list_new_game) }

        played_games_list_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.played_games_list_settings -> {
                    model.openSettings()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
    }

    override fun render(viewState: PlayedGamesListViewState) =
        when (viewState) {
            is PlayedGamesListViewState.PlayedGames -> playedGamesListAdapter.submitList(viewState.playedGamesList)
            is PlayedGamesListViewState.ShouldScrollToTop -> if (viewState.shouldScrollToTop) player_games_list_recycler_view.scrollToStart() else Unit
        }
}