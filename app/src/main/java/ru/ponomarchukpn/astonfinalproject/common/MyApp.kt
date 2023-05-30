package ru.ponomarchukpn.astonfinalproject.common

import android.app.Application
import ru.ponomarchukpn.astonfinalproject.di.DaggerApplicationComponent

class MyApp : Application() {

    val appComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
    }

    companion object {

        lateinit var myApp: MyApp
    }
}
