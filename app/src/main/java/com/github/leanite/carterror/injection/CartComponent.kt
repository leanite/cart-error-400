package com.github.leanite.carterror.injection

import com.github.leanite.carterror.viewmodel.CartViewModelFactory

fun cart() = CartComponent.instance

interface CartComponent {
    val viewModelFactory: CartViewModelFactory

    companion object {
        var instance: CartComponent = CartModule()
    }
}

class CartModule : CartComponent {
    override val viewModelFactory: CartViewModelFactory
        get() =
            CartViewModelFactory(interactor().getCartInteractor, interactor().refreshCartInteractor)
}