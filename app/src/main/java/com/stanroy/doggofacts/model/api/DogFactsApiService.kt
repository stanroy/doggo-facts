package com.stanroy.doggofacts.model.api

import com.stanroy.doggofacts.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// retrofit api service with specified endpoints
interface DogFactsApiService {

    @GET("dogs?number=30")
    suspend fun fetch30RandomFacts(): Response<DogFactsResponse>

}