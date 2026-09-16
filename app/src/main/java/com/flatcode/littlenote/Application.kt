package com.flatcode.littlenote

import android.app.Application
import androidx.appfunctions.AppFunctionConfiguration
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.flatcode.littlenote.functions.NoteFunctions
import dagger.hilt.android.HiltAndroidApp
import io.selimdawa.multicolors.MultiColorManager
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Application : Application(), AppFunctionConfiguration.Provider, Configuration.Provider {

    @Inject
    lateinit var noteFunctions: NoteFunctions

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
        MultiColorManager.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val appFunctionConfiguration: AppFunctionConfiguration =
        AppFunctionConfiguration.Builder()
            .addEnclosingClassFactory(NoteFunctions::class.java) { noteFunctions }.build()
}