package com.hr.warzy.bela.currentgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hr.warzy.bela.R
import kotlinx.android.synthetic.main.item_current_game_end_of_turn.view.*
import kotlinx.android.synthetic.main.item_played_games_list.view.*

enum class CurrentGameTurnItemTypes {
    TURN, END_OF_TURN
}

sealed class CurrentGameTurnItems(open val id: Long) {
    data class TurnItem(
        override val id: Long,
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
    ) : CurrentGameTurnItems(id)

    data class EndOfTurnItem(
        override val id: Long,
        val endOfTurnOurScore: Int,
        val endOfTurnYourScore: Int
    ) : CurrentGameTurnItems(id)
}

class CurrentGameTurnAdapter(
    private val layoutInflater: LayoutInflater,
    private val onItemClickedListener: (Long) -> Unit
) :
    ListAdapter<CurrentGameTurnItems, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<CurrentGameTurnItems>() {
        override fun areItemsTheSame(oldItem: CurrentGameTurnItems, newItem: CurrentGameTurnItems): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CurrentGameTurnItems, newItem: CurrentGameTurnItems): Boolean =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (CurrentGameTurnItemTypes.values()[viewType]) {
            CurrentGameTurnItemTypes.TURN -> OneTurnViewHolder(
                layoutInflater.inflate(R.layout.item_played_games_list, parent, false),
                onItemClickedListener
            )
            CurrentGameTurnItemTypes.END_OF_TURN -> EndOfTurnViewHolder(layoutInflater.inflate(R.layout.item_current_game_end_of_turn, parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (CurrentGameTurnItemTypes.values()[holder.itemViewType]) {
            CurrentGameTurnItemTypes.TURN -> (holder as OneTurnViewHolder).render(getItem(position) as CurrentGameTurnItems.TurnItem)
            CurrentGameTurnItemTypes.END_OF_TURN -> (holder as EndOfTurnViewHolder).render(getItem(position) as CurrentGameTurnItems.EndOfTurnItem)
        }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrentGameTurnItems.TurnItem -> CurrentGameTurnItemTypes.TURN.ordinal
            is CurrentGameTurnItems.EndOfTurnItem -> CurrentGameTurnItemTypes.END_OF_TURN.ordinal
        }
    }
}

class OneTurnViewHolder(itemView: View, private val onItemClickedListener: (Long) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val ourScoreView = itemView.item_played_games_list_mi_score
    private val yourScoreView = itemView.item_played_games_list_vi_score
    private val ourScoreLabelView = itemView.item_played_games_list_mi
    private val yourScoreLabelView = itemView.item_played_games_list_vi

    fun render(turnItem: CurrentGameTurnItems.TurnItem) =
        with(turnItem) {
            itemView.setOnClickListener { onItemClickedListener(turnItem.id) }
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

class EndOfTurnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ourScoreView = itemView.current_game_end_of_turn_mi_score
    private val yourScoreView = itemView.current_game_end_of_turn_vi_score

    fun render(endOfTurnItem: CurrentGameTurnItems.EndOfTurnItem) {
        ourScoreView.text = endOfTurnItem.endOfTurnOurScore.toString()
        yourScoreView.text = endOfTurnItem.endOfTurnYourScore.toString()
    }
}

