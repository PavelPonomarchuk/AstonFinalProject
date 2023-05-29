package ru.ponomarchukpn.astonfinalproject.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ponomarchukpn.astonfinalproject.presentation.screens.CharacterDetailsFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.CharactersFilterFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.CharactersFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.EpisodeDetailsFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.EpisodesFilterFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.EpisodesFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.LocationDetailsFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.LocationsFilterFragment
import ru.ponomarchukpn.astonfinalproject.presentation.screens.LocationsFragment
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

    fun inject(charactersFragment: CharactersFragment)
    fun inject(characterDetailsFragment: CharacterDetailsFragment)
    fun inject(charactersFilterFragment: CharactersFilterFragment)
    fun inject(locationsFragment: LocationsFragment)
    fun inject(locationDetailsFragment: LocationDetailsFragment)
    fun inject(locationsFilterFragment: LocationsFilterFragment)
    fun inject(episodesFragment: EpisodesFragment)
    fun inject(episodeDetailsFragment: EpisodeDetailsFragment)
    fun inject(episodesFilterFragment: EpisodesFilterFragment)

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
