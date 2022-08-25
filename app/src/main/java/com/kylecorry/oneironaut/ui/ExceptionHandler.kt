package com.kylecorry.oneironaut.ui

import android.content.Context
import com.kylecorry.andromeda.alerts.Alerts
import com.kylecorry.andromeda.core.system.CurrentApp
import com.kylecorry.andromeda.core.system.Intents
import com.kylecorry.andromeda.core.tryOrLog
import com.kylecorry.andromeda.files.LocalFiles
import com.kylecorry.oneironaut.R

object ExceptionHandler {

    fun initialize(activity: MainActivity) {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            recordException(activity, throwable)
            tryOrLog {
                CurrentApp.restart(activity)
            }
        }
        handleLastException(activity)
    }

    private fun handleLastException(context: Context) {
        val file = LocalFiles.getFile(context, FILENAME, create = false)
        if (!file.exists()) {
            return
        }
        val body = LocalFiles.read(context, FILENAME)
        LocalFiles.delete(context, FILENAME)

        Alerts.dialog(
            context,
            context.getString(R.string.error_occurred),
            context.getString(
                R.string.error_occurred_message,
                context.getString(R.string.app_name)
            ),
            okText = context.getString(R.string.email_developer)
        ) { cancelled ->
            if (!cancelled) {
                val intent = Intents.email(
                    context.getString(R.string.email),
                    "Error in ${context.getString(R.string.app_name)}",
                    body
                )
                context.startActivity(intent)
            }
        }
    }

    private fun recordException(activity: MainActivity, throwable: Throwable) {
        val message = throwable.message ?: ""
        val stackTrace = throwable.stackTraceToString()
        val details = "Message: ${message}\n\n$stackTrace"
        LocalFiles.write(activity, FILENAME, details, false)
    }

    private const val FILENAME = "errors/error.txt"

}