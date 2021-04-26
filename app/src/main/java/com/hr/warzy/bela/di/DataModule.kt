package com.hr.warzy.bela.di

import androidx.room.Room
import com.hr.warzy.data.GamesDatabase
import com.hr.warzy.data.GamesRepositoryImpl
import com.hr.warzy.domain.GamesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

fun dataModule(): Module = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            GamesDatabase::class.java, "games"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { (get<GamesDatabase>()).gameDao() }

    single { (get<GamesDatabase>()).turnDao() }

    single { GamesRepositoryImpl(get(), get(), get()) as GamesRepository }
}