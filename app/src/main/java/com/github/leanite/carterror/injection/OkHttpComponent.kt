package com.github.leanite.carterror.injection

import com.github.leanite.carterror.injection.OkHttpComponent.Companion.CACHE_SIZE_10_MB
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.SECONDS

fun okHttp() = OkHttpComponent.instance

interface OkHttpComponent {
    companion object {
        const val CACHE_SIZE_10_MB: Long = 10 * 1024 * 1024
        var instance: OkHttpComponent = OkHttpModule()
    }

    val client: OkHttpClient
}

class OkHttpModule : OkHttpComponent {
    override val client: OkHttpClient
        get(){
            val cacheFile = app().cacheDir
            return if (cacheFile != null) {
                val cache = Cache(cacheFile, CACHE_SIZE_10_MB)
                OkHttpClient.Builder().readTimeout(4, SECONDS).cache(cache).build()
            } else {
                OkHttpClient.Builder().readTimeout(4, SECONDS).build()
            }
        }
}