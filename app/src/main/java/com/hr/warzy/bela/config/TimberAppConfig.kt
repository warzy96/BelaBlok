package com.hr.warzy.bela.config

import android.util.Log
import com.hr.warzy.bela.BuildConfig
import timber.log.Timber

class TimberAppConfig {

    fun configure() {
        when (BuildConfig.BUILD_TYPE) {
            "debug" -> {
                Timber.plant(DebugTree())
            }
            "release" -> {
                Timber.plant(DebugTree())
            }
            else -> throw IllegalStateException("Logging not set for ${BuildConfig.BUILD_TYPE} build type")
        }
    }
}

/**
 * Extends [Timber.DebugTree] but:
 * - Tag is a link to the log
 * - Throws exception if priority == [Log.ERROR]
 */
private open class DebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.ERROR) {
            throw throwable ?: RuntimeException(message)
        }

        super.log(priority, tag, message, throwable)
    }

    override fun createStackElementTag(element: StackTraceElement) =
        "(${element.fileName}:${element.lineNumber})#${element.methodName} ${Thread.currentThread().name} DebugTree"
}