package com.stanroy.doggofacts.model.mock

import com.stanroy.doggofacts.model.api.DogFactItem
import com.stanroy.doggofacts.model.repositories.DoggoRepository
import com.stanroy.doggofacts.model.utils.NetworkResponse

class FakeDoggoRepository : DoggoRepository {

    private var factList = MockFactList.factList
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun clearFactList() {
        factList = arrayListOf()
    }

    override suspend fun fetch30RandomFacts(isUserOnline: Boolean): NetworkResponse<ArrayList<DogFactItem>> {
        return if (isUserOnline) {
            if (shouldReturnNetworkError) {
                NetworkResponse.Error("ERROR")
            } else {
                NetworkResponse.Success(factList, "SUCCESS")
            }
        } else {
            NetworkResponse.NoInternetConnection("NO_INTERNET_CONNECTION")
        }
    }
}