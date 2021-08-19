package com.stanroy.doggofacts.di

import com.stanroy.doggofacts.model.api.DogFactsApiService
import com.stanroy.doggofacts.model.sources.remote.RemoteDataSource
import com.stanroy.doggofacts.model.sources.remote.RemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object DataSourceModule {

    val provider: Module = module {
        single { provideRemoteDataSource(get()) }
    }

    private fun provideRemoteDataSource(dogFactsApiService: DogFactsApiService): RemoteDataSource {
        return RemoteDataSourceImpl(dogFactsApiService)
    }

}