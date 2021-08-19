package com.stanroy.doggofacts.model.mock

import com.stanroy.doggofacts.model.utils.CheckNetworkConnection

class FakeNetworkCheck : CheckNetworkConnection {

    private var isUserOnline: Boolean = true

    fun setShouldUserBeOnline(value: Boolean) {
        isUserOnline = value
    }

    override fun isNetworkAvailable(): Boolean {
        return isUserOnline
    }
}