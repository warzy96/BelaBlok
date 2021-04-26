package com.hr.warzy.bela.playedgameslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hr.warzy.bela.R
import com.hr.warzy.bela.view.RecyclerViewRemovableItemAdapter
import kotlinx.android.synthetic.main.item_played_games_list.view.*
import kotlinx.android.synthetic.main.item_played_games_list_timestamp.view.*

enum class PlayedGameViewTypes {
    PLAYED_GAME_ITEM, TIMESTAMP
}

sealed class PlayedGameItems(val id: Long) {
    data class ItemPlayedGame(val gameId: Long, val ourScore: Int, val yourScore: Int) : PlayedGameItems(gameId)
    data class TimeStamp(val timestamp: String) : PlayedGameItems(timestamp.hashCode().toLong())
}

class PlayedGamesListAdapter(
    private val layoutInflater: LayoutInflater,
    private val onItemRemovedListener: (Long) -> Unit,
    private val onItemClickedListener: (Long) -> Unit
) :
    ListAdapter<PlayedGameItems, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<PlayedGameItems>() {
            override fun areItemsTheSame(oldItem: PlayedGameItems, newItem: PlayedGameItems): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PlayedGameItems, newItem: PlayedGameItems): Boolean =
                oldItem == newItem
        }), RecyclerViewRemovableItemAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (PlayedGameViewTypes.values()[viewType]) {
            PlayedGameViewTypes.PLAYED_GAME_ITEM -> PlayedGameViewHolder(
                layoutInflater.inflate(R.layout.item_played_games_list, parent, false),
                onItemClickedListener
            )
            PlayedGameViewTypes.TIMESTAMP -> TimestampViewHolder(layoutInflater.inflate(R.layout.item_played_games_list_timestamp, parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is PlayedGameItems.ItemPlayedGame -> (holder as PlayedGameViewHolder).render(item)
            is PlayedGameItems.TimeStamp -> (holder as TimestampViewHolder).render(item)
        }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PlayedGameItems.ItemPlayedGame -> PlayedGameViewTypes.PLAYED_GAME_ITEM.ordinal
            is PlayedGameItems.TimeStamp -> PlayedGameViewTypes.TIMESTAMP.ordinal
            else -> throw RuntimeException("Item type not supported!")
        }

    override fun removeItem(adapterPosition: Int) = onItemRemovedListener((getItem(adapterPosition) as PlayedGameItems.ItemPlayedGame).gameId)
}

class PlayedGameViewHolder(itemView: View, private val onItemClickedListener: (Long) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val ourScoreView = itemView.item_played_games_list_mi_score
    private val yourScoreView = itemView.item_played_games_list_vi_score
    private val ourScoreLabelView = itemView.item_played_games_list_mi
    private val yourScoreLabelView = itemView.item_played_games_list_vi

    fun render(itemPlayedGame: PlayedGameItems.ItemPlayedGame) =
        with(itemPlayedGame) {
            itemView.setOnClickListener { onItemClickedListener(gameId) }
            ourScoreView.text = ourScore.toString()
            yourScoreView.text = yourScore.toString()

            val winningColor = itemView.context.getColor(R.color.colorAccent)
            val commonTextColor = itemView.context.getColor(R.color.common_text_color)
            if (ourScore > yourScore) {
                ourScoreView.setTextColor(winningColor)
                ourScoreLabelView.setTextColor(winningColor)
                yourScoreLabelView.setTextColor(commonTextColor)
                yourScoreView.setTextColor(commonTextColor)
            } else if (ourScore < yourScore) {
                yourScoreView.setTextColor(winningColor)
                yourScoreLabelView.setTextColor(winningColor)
                ourScoreView.setTextColor(commonTextColor)
                ourScoreLabelView.setTextColor(commonTextColor)
            }
        }
}

class TimestampViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val timestampView = itemView.item_played_games_list_timestamp

    fun render(timestamp: PlayedGameItems.TimeStamp) {
        timestampView.text = timestamp.timestamp
    }
}