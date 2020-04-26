package com.github.leanite.carterror.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

data class CartError(
    @SerializedName("data")
    val cart: Cart,

    @SerializedName("errorMessage")
    val errorMessage: String) {

    companion object {
        fun mapFromException(ex: HttpException): CartError? {
            val errorBody = ex.response()?.errorBody()?.string()
            return if (errorBody != null) {
                Gson().fromJson(errorBody, CartError::class.java)
            } else {
                null
            }
        }
    }
}