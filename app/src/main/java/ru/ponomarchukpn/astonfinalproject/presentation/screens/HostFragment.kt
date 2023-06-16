package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import com.google.android.material.navigation.NavigationBarView
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentHostBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.HostViewModel

class HostFragment : BaseFragment<FragmentHostBinding, HostViewModel>(
    HostViewModel::class.java
) {

    //при движении назад по вкладкам должны подсвечиваться табы

    private val navigationListener = NavigationBarView.OnItemSelectedListener {
        val fragment = when (it.itemId) {
            R.id.menu_item_characters -> CharactersFragment.newInstance()
            R.id.menu_item_locations -> LocationsFragment.newInstance()
            R.id.menu_item_episodes -> EpisodesFragment.newInstance()
            else -> throw RuntimeException("Unknown menu item id: ${it.itemId}")
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.host_container, fragment)
            .addToBackStack(null)
            .commit()
        true
    }

    override fun createBinding(): FragmentHostBinding {
        return FragmentHostBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setNavigationListener
        binding.hostNavigation.setOnItemSelectedListener(navigationListener)

        //setDefaultFragment
        childFragmentManager.beginTransaction()
            .replace(R.id.host_container, CharactersFragment.newInstance())
            .commit()
    }

    companion object {

        fun newInstance() = HostFragment()
    }
}
