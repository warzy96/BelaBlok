package com.hr.warzy.bela

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hr.warzy.bela.navigation.Router
import com.hr.warzy.bela.navigation.RoutingActionConsumer
import com.hr.warzy.bela.navigation.RoutingActionsSource
import com.hr.warzy.bela.view.hideKeyboard
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), RoutingActionConsumer {

    private val router: Router by inject { parametersOf(this) }
    private val routingActionsSource: RoutingActionsSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router.showGamesScreen()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        routingActionsSource.setActiveRoutingActionConsumer(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        routingActionsSource.unsetRoutingActionConsumer(this)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        if (shouldUnsetRoutingActionConsumer()) {
            routingActionsSource.unsetRoutingActionConsumer(this)
        }
        super.onPause()
    }

    private fun shouldUnsetRoutingActionConsumer() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) isInMultiWindowMode.not() else true

    override fun onRoutingAction(routingAction: (Router) -> Unit) {
        runOnUiThread {
            currentFocus?.hideKeyboard()
        }
        routingAction(router)
    }
}