package com.github.leanite.carterror.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("product")
    val product: Product,

    @SerializedName("quantity")
    val quantity: Int
)