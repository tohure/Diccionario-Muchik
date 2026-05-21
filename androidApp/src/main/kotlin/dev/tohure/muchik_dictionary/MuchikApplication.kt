package dev.tohure.muchik_dictionary

import android.app.Application
import dev.tohure.muchik_dictionary.core.di.initKoin

class MuchikApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
