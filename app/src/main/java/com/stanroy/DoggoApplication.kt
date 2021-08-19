package com.stanroy

import android.app.Application
import com.stanroy.doggofacts.di.DataSourceModule
import com.stanroy.doggofacts.di.NetworkModule
import com.stanroy.doggofacts.di.RepositoryModule
import com.stanroy.doggofacts.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DoggoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DoggoApplication)

            koin.loadModules(
                listOf(
                    DataSourceModule.provider,
                    RepositoryModule.provider,
                    NetworkModule.provider,
                    ViewModelModule.provider
                )
            )
        }
    }

}