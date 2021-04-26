package com.hr.warzy.bela.navigation

import androidx.appcompat.app.AppCompatActivity
import org.koin.core.module.Module
import org.koin.dsl.module

fun navigationModule(): Module = module {
    factory { (activity: AppCompatActivity) -> RouterImpl(activity, activity.supportFragmentManager) as Router }

    single<RoutingMediator> { RoutingMediatorImpl() }

    single<RoutingActionsDispatcher> { get<RoutingMediator>() }
    single<RoutingActionsSource> { get<RoutingMediator>() }
}