package ru.ponomarchukpn.astonfinalproject.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ponomarchukpn.astonfinalproject.presentation.screens.MainViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class])
interface ApplicationComponent {

    fun inject(viewModel: MainViewModel)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}
