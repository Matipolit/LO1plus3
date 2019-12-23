package pl.matmar.matipolit.lo1plus

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.NetworkConnectionInterceptor
import pl.matmar.matipolit.lo1plus.data.repositories.GradesRepository
import pl.matmar.matipolit.lo1plus.data.repositories.HomeRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import pl.matmar.matipolit.lo1plus.ui.auth.AuthViewModelFactory
import pl.matmar.matipolit.lo1plus.ui.grades.GradesViewModelFactory
import pl.matmar.matipolit.lo1plus.ui.home.HomeViewModelFactory
import timber.log.Timber

class LO1App : Application(), KodeinAware {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val kodein: Kodein
        get() = Kodein.lazy {
            import(androidXModule(this@LO1App))
            bind() from singleton { NetworkConnectionInterceptor(instance()) }
            bind() from singleton { MyApi(instance()) }
            bind() from singleton { LO1Database(instance()) }
            bind() from singleton { UserRepository(instance(), instance()) }
            bind() from singleton { HomeRepository(instance(), instance())}
            bind() from singleton { GradesRepository(instance(), instance()) }

            bind() from provider { AuthViewModelFactory(instance()) }
            bind() from provider { HomeViewModelFactory(instance(), instance()) }
            bind() from provider { GradesViewModelFactory(instance(), instance()) }
            //bind() from factory {user: User -> HomeViewModelFactory(instance(), user)}
        }
}