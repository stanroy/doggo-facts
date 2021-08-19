package com.stanroy.doggofacts.model.api


import com.google.gson.annotations.SerializedName

// single item received from api
data class DogFactItem(
    @SerializedName("fact")
    val fact: String,
    var updateDate: Long
)