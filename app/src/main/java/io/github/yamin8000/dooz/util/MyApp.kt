package io.github.yamin8000.dooz.util

import android.app.Application
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

@Suppress("unused")
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            prepareLogger()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG)
                Log.d(Constants.LOG_TAG, e.stackTraceToString())
        }
    }

    private fun prepareLogger() {
        Logger.addLogAdapter(
            AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder().tag(Constants.LOG_TAG).build()
            )
        )
        Logger.i("Game is Started!")
    }
}