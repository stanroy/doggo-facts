package com.stanroy.doggofacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stanroy.doggofacts.model.api.DogFactItem
import com.stanroy.doggofacts.model.repositories.DoggoRepository
import com.stanroy.doggofacts.model.utils.CheckNetworkConnection
import com.stanroy.doggofacts.model.utils.FactListStatus
import com.stanroy.doggofacts.model.utils.FactListStatus.*
import com.stanroy.doggofacts.model.utils.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FactListViewModel(
    private val doggoRepository: DoggoRepository,
    private val networkCheck: CheckNetworkConnection
) : ViewModel() {

    private val viewModelJob = Job()
    private val mainScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _storedFacts = MutableLiveData<ArrayList<DogFactItem>>()
    val storedFacts: LiveData<ArrayList<DogFactItem>>
        get() = _storedFacts

    // I'm decoupling status from NetworkResponse, so that Fragment will only operate on said status
    // and provided message. This way it does not have unnecessary access to data and only receives it
    // when necessary (Successful response)
    private val _status = MutableLiveData<Pair<FactListStatus, String>>()
    val status: LiveData<Pair<FactListStatus, String>>
        get() = _status


    init {
        fetchFacts(true)
    }

    fun onRefreshClicked() {
        // Load 30 more facts
        fetchFacts(false)
    }

    fun onReloadClicked() {
        // Try to reload facts after encountering an error
        fetchFacts(false)
    }

    private fun fetchFacts(initialLoad: Boolean) {
        mainScope.launch {
            // Set status to Loading, Fragment will show ProgressBar
            _status.postValue(Pair(FactListStatus.LOADING, "Loading..."))
            // Make an repository call
            when (val call = doggoRepository.fetch30RandomFacts(isUserOnline())) {
                is NetworkResponse.Success -> {
                    call.data.let { data ->
                        // Successful call, if it's first time loading get 30 facts
                        if (initialLoad) {
                            _storedFacts.postValue(data)
                            _status.postValue(Pair(SUCCESS, call.message))
                        } else {
                            // If it's not initial load, get old data and append new data to it
                            val oldData = _storedFacts.value
                            val newData = arrayListOf<DogFactItem>()
                            oldData?.let { newData.addAll(it) }
                            newData.addAll(data)
                            // Post combined data
                            _storedFacts.postValue(newData)
                            // No errors encountered, Fragment will hide ProgressBar
                            _status.postValue(Pair(SUCCESS, call.message))
                        }
                    }
                }
                // Catch errors, set status to ERROR and provide appropriate message
                is NetworkResponse.Error -> {
                    _status.postValue(Pair(ERROR, call.message))
                }
                // User is offline, set status to NO_INTERNET_CONNECTION and provide appropriate message
                is NetworkResponse.NoInternetConnection -> {
                    _status.postValue(Pair(NO_INTERNET_CONNECTION, call.message))
                }
            }
        }
    }


    private fun isUserOnline(): Boolean {
        return networkCheck.isNetworkAvailable()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}