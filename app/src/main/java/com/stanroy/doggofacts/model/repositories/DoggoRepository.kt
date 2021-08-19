package com.stanroy.doggofacts.model.repositories

import com.stanroy.doggofacts.model.api.DogFactItem
import com.stanroy.doggofacts.model.utils.NetworkResponse

interface DoggoRepository {

    suspend fun fetch30RandomFacts(isUserOnline: Boolean): NetworkResponse<ArrayList<DogFactItem>>

}