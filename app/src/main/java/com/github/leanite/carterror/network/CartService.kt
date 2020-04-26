package com.github.leanite.carterror.network

import com.github.leanite.carterror.model.Cart
import retrofit2.http.GET
import retrofit2.http.PUT

interface CartService {
    @GET("cart")
    suspend fun getCart() : Cart

    @PUT("cart")
    suspend fun refreshCart() : Cart
}
