package dev.tohure.muchik_dictionary

import android.app.Application
import dev.tohure.muchik_dictionary.core.di.initKoin
import dev.tohure.muchik_dictionary.core.di.platformModule
import org.koin.android.ext.koin.androidContext

class MuchikApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(platformModule) { androidContext(this@MuchikApplication) }
    }
}
