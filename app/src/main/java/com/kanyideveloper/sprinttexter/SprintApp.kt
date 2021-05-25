package com.kanyideveloper.sprinttexter

import android.app.Application
import timber.log.Timber

class SprintApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}