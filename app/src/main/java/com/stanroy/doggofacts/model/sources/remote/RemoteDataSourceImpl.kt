package com.stanroy.doggofacts.model.sources.remote

import com.stanroy.doggofacts.model.api.DogFactsApiService
import com.stanroy.doggofacts.model.api.DogFactsResponse
import retrofit2.Response

class RemoteDataSourceImpl(private val dogFactsApiService: DogFactsApiService) : RemoteDataSource {
    override suspend fun fetch30RandomFacts(): Response<DogFactsResponse> {
        return dogFactsApiService.fetch30RandomFacts()
    }
}