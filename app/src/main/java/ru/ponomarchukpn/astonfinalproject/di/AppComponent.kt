package ru.ponomarchukpn.astonfinalproject.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ponomarchukpn.astonfinalproject.presentation.screens.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DatabaseModule::class,
        NetworkModule::class,
        DomainModule::class,
        ViewModelsModule::class
    ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    companion object {

        fun init(context: Context): AppComponent {
            return DaggerAppComponent.factory()
                .create(context)
        }
    }

    @Component.Factory
    interface AppComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
