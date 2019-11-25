package pl.matmar.matipolit.lo1plus

import android.app.Application
import timber.log.Timber

class LO1App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}