package com.stanroy.doggofacts.model.sources.remote

import com.stanroy.doggofacts.model.api.DogFactsResponse
import retrofit2.Response

interface RemoteDataSource {

    suspend fun fetch30RandomFacts() : Response<DogFactsResponse>

}