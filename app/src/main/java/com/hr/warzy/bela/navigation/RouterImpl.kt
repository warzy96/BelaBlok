package com.hr.warzy.bela.navigation

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hr.warzy.bela.R
import com.hr.warzy.bela.currentgame.CurrentGameFragment
import com.hr.warzy.bela.playedgameslist.PlayedGamesListFragment
import com.hr.warzy.bela.settings.SettingsFragment
import com.hr.warzy.bela.turn.TurnFragment
import com.hr.warzy.bela.view.CurrentGameToNewTurn
import com.hr.warzy.bela.view.PlayedGamesListItemToCurrentGame

private const val MAIN_ACTIVITY_CONTAINER = R.id.main_activity_container

class RouterImpl(
    private val activity: AppCompatActivity,
    private val fragmentManager: FragmentManager
) : Router {

    override fun showGamesScreen() {
        fragmentManager.beginTransaction()
            .add(MAIN_ACTIVITY_CONTAINER, PlayedGamesListFragment(), PlayedGamesListFragment.TAG)
            .commit()
    }

    override fun openNewGameScreen(sharedView: View) {
        fragmentManager.beginTransaction()
            .addSharedElement(sharedView, sharedView.transitionName)
            .replace(MAIN_ACTIVITY_CONTAINER, CurrentGameFragment(), CurrentGameFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun openCurrentGameScreen(gameId: Long) {
        fragmentManager.beginTransaction()
            .applyRightEnterAndLeftExitAnimation()
            .add(MAIN_ACTIVITY_CONTAINER, CurrentGameFragment.newInstance(gameId), CurrentGameFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun openNewTurnScreen(gameId: Long, sharedView: View) {
        fragmentManager.beginTransaction()
            .addSharedElement(sharedView, sharedView.transitionName)
            .replace(MAIN_ACTIVITY_CONTAINER, TurnFragment.newInstance(gameId), TurnFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun openExistingTurnScreen(gameId: Long, turnId: Long) {
        fragmentManager.beginTransaction()
            .applyRightEnterAndLeftExitAnimation()
            .add(MAIN_ACTIVITY_CONTAINER, TurnFragment.newInstance(gameId, turnId), TurnFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun goBack() = goBackInternal()

    private fun goBackInternal() {
        if (fragmentManager.backStackEntryCount > 0) fragmentManager.popBackStackImmediate()
        else activity.finish()
    }

    override fun openSettings() {
        fragmentManager.beginTransaction()
            .applyBottomSlideEnterAndExitAnimation()
            .add(MAIN_ACTIVITY_CONTAINER, SettingsFragment(), SettingsFragment.TAG)
            .addToBackStack(null)
            .commit()
    }
}

fun FragmentTransaction.applyBottomSlideEnterAndExitAnimation(): FragmentTransaction {
    setCustomAnimations(R.anim.fragment_bottom_enter, R.anim.nothing, R.anim.nothing, R.anim.fragment_bottom_exit)

    return this
}

fun FragmentTransaction.applyRightEnterAndLeftExitAnimation(): FragmentTransaction {
    setCustomAnimations(
        R.anim.fragment_right_enter,
        R.anim.fragment_left_exit,
        R.anim.fragment_left_enter,
        R.anim.fragment_right_exit
    )

    return this
}

fun FragmentTransaction.applyFadeInEnterAndFadeOutExitAnimation(): FragmentTransaction {
    setCustomAnimations(
        R.anim.fragment_fade_in_and_translate,
        R.anim.fragment_fade_out_and_scale_out,
        R.anim.fragment_fade_in_and_scale_in,
        R.anim.fragment_fade_out_and_translate
    )

    return this
}