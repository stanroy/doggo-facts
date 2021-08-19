package com.stanroy.doggofacts.model.repositories

import android.content.Context
import com.stanroy.doggofacts.R
import com.stanroy.doggofacts.model.api.DogFactItem
import com.stanroy.doggofacts.model.sources.remote.RemoteDataSource
import com.stanroy.doggofacts.model.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class DoggoRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val context: Context
) : DoggoRepository {

    override suspend fun fetch30RandomFacts(isUserOnline: Boolean): NetworkResponse<ArrayList<DogFactItem>> =
        withContext(Dispatchers.IO) {

            val resources = context.resources
            val factArrayList: ArrayList<DogFactItem>
            var networkResponse: NetworkResponse<ArrayList<DogFactItem>>

            // checking if user has internet connection
            if (isUserOnline) {
                try {
                    // making an api call
                    val call = remoteDataSource.fetch30RandomFacts()
                    // if call is successful pass NetworkResponse as Success
                    if (call.isSuccessful) {
                        factArrayList = ArrayList(call.body())
                        factArrayList.forEach { fact ->
                            fact.updateDate = System.currentTimeMillis()
                        }
                        networkResponse = NetworkResponse.Success(
                            factArrayList,
                            resources.getString(
                                R.string.success_network_response_template,
                                call.code(),
                                call.message()
                            )
                        )
                    } else {
                        // retrieving api error messages along with error code
                        networkResponse =
                            NetworkResponse.Error(
                                resources.getString(
                                    R.string.error_network_response_template,
                                    call.code(),
                                    call.message()
                                )
                            )
                    }
                    // retrieving other errors
                } catch (e: Exception) {
                    networkResponse = NetworkResponse.Error(e.message.toString())
                }
            } else {
                // user is offline, provide appropriate error
                networkResponse =
                    NetworkResponse.NoInternetConnection(resources.getString(R.string.error_no_internet))
            }
            // return network response variable
            return@withContext networkResponse
        }
}