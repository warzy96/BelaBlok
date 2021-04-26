package com.hr.warzy.bela

import android.app.Application
import com.hr.warzy.bela.base.threadingModule
import com.hr.warzy.bela.config.TimberAppConfig
import com.hr.warzy.bela.config.configModule
import com.hr.warzy.bela.currentgame.currentGameModule
import com.hr.warzy.bela.di.dataModule
import com.hr.warzy.bela.navigation.navigationModule
import com.hr.warzy.bela.playedgameslist.playedGamesModule
import com.hr.warzy.bela.turn.turnModule
import com.hr.warzy.domain.interactor.domainModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BelaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BelaApplication)
            modules(
                applicationModule(),
                dataModule(),
                turnModule(),
                playedGamesModule(),
                navigationModule(),
                threadingModule(),
                currentGameModule(),
                configModule(),
                domainModule()
            )
        }

        get<TimberAppConfig>().configure()
    }
}