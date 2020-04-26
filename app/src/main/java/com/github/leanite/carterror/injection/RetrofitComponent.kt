package com.github.leanite.carterror.injection

import com.github.leanite.carterror.injection.RetrofitComponent.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun retrofit() = RetrofitComponent.instance

interface RetrofitComponent {
    companion object {
        const val BASE_URL = "http://192.168.0.9:8080"
        var instance: RetrofitComponent = RetrofitModule()
    }

    val client: Retrofit
}

class RetrofitModule : RetrofitComponent {
    override val client: Retrofit
        get() {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp().client)
                    .build()
        }
}