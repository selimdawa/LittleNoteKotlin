package com.flatcode.littlenote

import android.app.Application
import android.text.format.DateFormat
import androidx.appfunctions.AppFunctionConfiguration
import com.flatcode.littlenote.functions.NoteFunctions
import dagger.hilt.android.HiltAndroidApp
import io.selimdawa.multicolors.MultiColorManager
import timber.log.Timber
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class Application : Application(), AppFunctionConfiguration.Provider {

    @Inject lateinit var noteFunctions: NoteFunctions

    override fun onCreate() {
        super.onCreate()
        MultiColorManager.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val appFunctionConfiguration: AppFunctionConfiguration =
        AppFunctionConfiguration.Builder()
            .addEnclosingClassFactory(NoteFunctions::class.java) { noteFunctions }
            .build()

    companion object {
        fun formatTimestamp(timestamp: Long): String {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp
            return DateFormat.format("dd/MM/yyyy", calendar).toString()
        }
    }
}