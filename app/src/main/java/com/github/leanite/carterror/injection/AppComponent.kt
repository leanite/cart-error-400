package com.github.leanite.carterror.injection

import android.app.Application
import java.io.File

fun app() = AppComponent.instance

interface AppComponent {
    companion object {
        var instance: AppComponent = AppModule()
    }

    var application: Application?
    var cacheDir: File?

    fun setup(app: Application) {
        application = app
        cacheDir = application?.cacheDir
    }
}

class AppModule : AppComponent {
    override var application: Application? = null
    override var cacheDir: File? = null
}