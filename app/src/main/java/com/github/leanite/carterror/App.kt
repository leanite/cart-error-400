package com.github.leanite.carterror

import android.app.Application
import com.github.leanite.carterror.injection.app

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        app().setup(this)
    }
}