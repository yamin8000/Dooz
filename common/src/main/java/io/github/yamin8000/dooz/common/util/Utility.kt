package io.github.yamin8000.dooz.common.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import io.github.yamin8000.dooz.common.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

object Utility {

    @Composable
    fun <T> ObserverEvent(
        flow: Flow<T>,
        onEvent: suspend (T) -> Unit
    ) {
        val lifeCycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(flow, lifeCycleOwner.lifecycle) {
            lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    flow.collect(onEvent)
                }
            }
        }
    }

    /** Prints [message] to logcat if app is in debug build */
    fun log(
        message: String
    ) {
        if (BuildConfig.DEBUG) {
            Log.d("", message)
        }
    }
}