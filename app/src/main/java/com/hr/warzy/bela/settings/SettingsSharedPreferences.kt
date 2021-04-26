package com.hr.warzy.bela.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Completable

private const val KEY_END_GAME_RESULT = "KEY_END_GAME_RESULT"
private const val END_GAME_RESULT_DEFAULT = 1001
const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
class SettingsSharedPreferences(private val sharedPreferences: SharedPreferences) {

    fun endGameResult(): Int = sharedPreferences.getInt(KEY_END_GAME_RESULT, END_GAME_RESULT_DEFAULT)

    @SuppressLint("ApplySharedPref")
    fun saveEndGameResult(endGameResult: Int): Completable =
        Completable.fromAction {
            sharedPreferences.edit()
                .putInt(KEY_END_GAME_RESULT, endGameResult)
                .commit()
        }
}