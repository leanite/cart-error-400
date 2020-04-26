package com.github.leanite.carterror.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.leanite.carterror.interaction.GetCartInteractor
import com.github.leanite.carterror.interaction.RefreshCartInteractor

class CartViewModelFactory(
    val getCartInteractor: GetCartInteractor,
    val refreshCartInteractor: RefreshCartInteractor) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CartViewModel(getCartInteractor, refreshCartInteractor) as T
}