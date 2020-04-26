package com.github.leanite.carterror.injection

import com.github.leanite.carterror.interaction.GetCartInteractor
import com.github.leanite.carterror.interaction.RefreshCartInteractor

fun interactor() = InteractorComponent.instance

interface InteractorComponent {
    companion object {
        var instance: InteractorComponent = InteractorModule()
    }

    val getCartInteractor: GetCartInteractor
    val refreshCartInteractor: RefreshCartInteractor
}

class InteractorModule : InteractorComponent {
    override val getCartInteractor: GetCartInteractor =
        GetCartInteractor(retrofit().client)

    override val refreshCartInteractor: RefreshCartInteractor =
        RefreshCartInteractor(retrofit().client)
}