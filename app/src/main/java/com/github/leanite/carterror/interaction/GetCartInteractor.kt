package com.github.leanite.carterror.interaction

import com.github.leanite.carterror.model.Cart
import com.github.leanite.carterror.network.CartService
import retrofit2.Retrofit
import java.lang.Exception

class GetCartInteractor(retrofit: Retrofit) {

    private val service = retrofit.create(CartService::class.java)

    suspend fun execute() = service.getCart()
}

sealed class GetCartEvent {
    data class Success(val cart: Cart) : GetCartEvent()
    data class Loading (val show: Boolean) : GetCartEvent()
    data class Error (val exception: Exception) : GetCartEvent()
}

//sealed class RegisterUserEvent {
//    data class Success(val user: User) : RegisterUserEvent()
//    data class Loading(val show: Boolean) : RegisterUserEvent()
//    data class Error (val exception: Exception) : RegisterUserEvent()
//    data class Timeout (val exception: Exception) : RegisterUserEvent()
//}