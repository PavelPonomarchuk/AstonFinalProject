package ru.ponomarchukpn.astonfinalproject.common

import android.app.Application
import ru.ponomarchukpn.astonfinalproject.di.App
import ru.ponomarchukpn.astonfinalproject.di.AppComponent

class MyApp : Application(), App {

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent()
    }

    override fun appComponent() = getAppComponent()

    private fun getAppComponent(): AppComponent {
        if (appComponent == null) {
            appComponent = AppComponent.init(applicationContext)
        }
        return appComponent!!
    }
}
