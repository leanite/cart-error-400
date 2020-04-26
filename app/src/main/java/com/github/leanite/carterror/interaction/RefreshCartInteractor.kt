package com.github.leanite.carterror.interaction

import com.github.leanite.carterror.model.Cart
import com.github.leanite.carterror.network.CartService
import retrofit2.Retrofit
import java.lang.Exception

class RefreshCartInteractor(retrofit: Retrofit) {

    private val service = retrofit.create(CartService::class.java)

    suspend fun execute() = service.refreshCart()
}

sealed class RefreshCartEvent {
    data class Success(val cart: Cart) : RefreshCartEvent()
    data class Loading (val show: Boolean) : RefreshCartEvent()
    data class Error (val errorMessage: String) : RefreshCartEvent()
}

//sealed class RegisterUserEvent {
//    data class Success(val user: User) : RegisterUserEvent()
//    data class Loading(val show: Boolean) : RegisterUserEvent()
//    data class Error (val exception: Exception) : RegisterUserEvent()
//    data class Timeout (val exception: Exception) : RegisterUserEvent()
//}