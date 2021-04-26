package com.hr.warzy.bela

import android.content.Context
import com.hr.warzy.bela.settings.SETTINGS_SHARED_PREFERENCES
import com.hr.warzy.bela.settings.SettingsSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

fun applicationModule(): Module = module {
    single { SettingsSharedPreferences(androidContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE)) }
}