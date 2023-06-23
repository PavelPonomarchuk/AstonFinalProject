package ru.ponomarchukpn.astonfinalproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharacterDetailsViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharactersFilterViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharactersViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodeDetailsViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodesFilterViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodesViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationDetailsViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationsFilterViewModel
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationsViewModel
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
    @ViewModelKey(CharactersViewModel::class)
    fun bindCharactersViewModel(charactersViewModel: CharactersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailsViewModel::class)
    fun bindCharacterDetailsViewModel(charactersDetailsViewModel: CharacterDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharactersFilterViewModel::class)
    fun bindCharactersFilterViewModel(charactersFilterViewModel: CharactersFilterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    fun bindLocationsViewModel(locationsViewModel: LocationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationDetailsViewModel::class)
    fun bindLocationDetailsViewModel(locationDetailsViewModel: LocationDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsFilterViewModel::class)
    fun bindLocationsFilterViewModel(locationsFilterViewModel: LocationsFilterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    fun bindEpisodesViewModel(episodesViewModel: EpisodesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodeDetailsViewModel::class)
    fun bindEpisodeDetailsViewModel(episodeDetailsViewModel: EpisodeDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesFilterViewModel::class)
    fun bindEpisodesFilterViewModel(episodesFilterViewModel: EpisodesFilterViewModel): ViewModel
}
