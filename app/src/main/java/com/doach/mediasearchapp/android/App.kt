package com.doach.mediasearchapp.android

import android.app.Application
import com.doach.mediasearchapp.android.di.AppContainer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

}