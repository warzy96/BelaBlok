package com.hr.warzy.bela.settings

import com.hr.warzy.bela.R
import com.hr.warzy.bela.base.BaseFragment
import org.koin.android.ext.android.inject

class SettingsFragment : BaseFragment<SettingsViewState>(R.layout.fragment_settings) {

    companion object {
        const val TAG = "SettingsFragment"
    }

    override val model: SettingsViewModel by inject()

    override fun render(viewState: SettingsViewState) {
        when (viewState) {
            is SettingsViewState.EndGameResult -> Unit
        }
    }

}