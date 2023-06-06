package ru.ponomarchukpn.astonfinalproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.MainViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.ViewModelFactory
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.ViewModelKey

@Module
interface ViewModelsModule {

    @Binds
    fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}
