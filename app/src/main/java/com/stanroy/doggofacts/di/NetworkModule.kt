package com.stanroy.doggofacts.di

import android.content.Context
import com.stanroy.doggofacts.BuildConfig
import com.stanroy.doggofacts.model.api.DogFactsApiService
import com.stanroy.doggofacts.model.utils.CheckNetworkConnection
import com.stanroy.doggofacts.model.utils.CheckNetworkConnectionImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    val provider: Module = module {
        single { provideLoggingInterceptor() }
        single { provideOkHttpClient(get()) }
        single { provideRetrofit(get()) }
        single { provideRetrofitService(get()) }
        single { provideNetworkCheck(androidContext()) }
    }

    private fun provideNetworkCheck(context: Context): CheckNetworkConnection {
        return CheckNetworkConnectionImpl(context)
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return loggingInterceptor
    }

    private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun provideRetrofitService(retrofit: Retrofit): DogFactsApiService {
        return retrofit.create(DogFactsApiService::class.java)
    }


}