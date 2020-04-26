package com.github.leanite.carterror.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double
)