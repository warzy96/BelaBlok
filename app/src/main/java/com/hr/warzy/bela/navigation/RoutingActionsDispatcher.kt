package com.hr.warzy.bela.navigation

interface RoutingActionsDispatcher {

    fun dispatch(routingAction: (Router) -> Unit)

    fun dispatchDistinct(actionId: String?, routingAction: (Router) -> Unit)
}