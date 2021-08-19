package com.stanroy.doggofacts.di

import android.content.Context
import com.stanroy.doggofacts.model.repositories.DoggoRepository
import com.stanroy.doggofacts.model.repositories.DoggoRepositoryImpl
import com.stanroy.doggofacts.model.sources.remote.RemoteDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object RepositoryModule {

    val provider: Module = module {
        single { provideRepository(get(), androidContext()) }
    }

    private fun provideRepository(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): DoggoRepository {
        return DoggoRepositoryImpl(remoteDataSource, context)
    }

}