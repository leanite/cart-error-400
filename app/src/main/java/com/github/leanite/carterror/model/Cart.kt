package com.github.leanite.carterror.model

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("items")
    val items: MutableList<Item>
)