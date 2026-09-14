package com.flatcode.littlenote

import android.app.Application
import android.text.format.DateFormat
import com.flatcode.littlenote.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import io.selimdawa.multicolors.MultiColorManager
import timber.log.Timber
import java.util.Calendar
import java.util.Locale

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiColorManager.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        fun formatTimestamp(timestamp: Long): String {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp
            return DateFormat.format("dd/MM/yyyy", calendar).toString()
        }
    }
}