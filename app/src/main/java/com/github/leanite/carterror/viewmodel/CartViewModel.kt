package com.github.leanite.carterror.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leanite.carterror.interaction.GetCartEvent
import com.github.leanite.carterror.interaction.GetCartInteractor
import com.github.leanite.carterror.interaction.RefreshCartEvent
import com.github.leanite.carterror.interaction.RefreshCartInteractor
import com.github.leanite.carterror.model.Cart
import com.github.leanite.carterror.model.CartError
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CartViewModel(
    private val getCartInteractor: GetCartInteractor,
    private val refreshCartInteractor: RefreshCartInteractor) : ViewModel() {

    var cart: Cart = Cart(mutableListOf())
    val getCartEvent = MutableLiveData<GetCartEvent>()
    val refreshCartEvent = MutableLiveData<RefreshCartEvent>()

    fun getCart() {
        viewModelScope.launch {
            getCartEvent.value = GetCartEvent.Loading(true)

            try {
                val response = getCartInteractor.execute()

                cart.items.clear()
                cart.items.addAll(response.items)

                getCartEvent.value = GetCartEvent.Success(cart)
            } catch (ex: Exception) {
                getCartEvent.value = (GetCartEvent.Error(ex))
            } finally {
                getCartEvent.value = GetCartEvent.Loading(false)
            }
        }
    }

    fun refreshCart() {
        viewModelScope.launch {
            refreshCartEvent.value = RefreshCartEvent.Loading(true)

            try {
                val response = refreshCartInteractor.execute()

                updateCart(response)
            } catch (httpException: HttpException) {
                when (httpException.code()) {
                    424 -> {
                        val cartError = CartError.mapFromException(httpException)
                        cartError?.let {
                            updateCart(it.cart)
                            showError(it.errorMessage)
                        }

                    }
                    else -> showError("Erro HTTP")
                }
            } finally {
                refreshCartEvent.value = RefreshCartEvent.Loading(false)
            }
        }
    }

    private fun updateCart(updatedCart: Cart) {
        cart.items.clear()
        cart.items.addAll(updatedCart.items)
        refreshCartEvent.value = RefreshCartEvent.Success(cart)
    }

    private fun showError(message: String) {
        refreshCartEvent.value = (RefreshCartEvent.Error(message))
    }
}