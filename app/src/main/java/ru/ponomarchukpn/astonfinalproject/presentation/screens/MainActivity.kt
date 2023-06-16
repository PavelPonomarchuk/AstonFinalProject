package ru.ponomarchukpn.astonfinalproject.presentation.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var selectedTabName: String? = null

    private val navigationListener = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.menu_item_characters -> {
                val startFragment = CharactersFragment.newInstance(TAB_NAME_CHARACTERS)
                showTab(startFragment, TAB_NAME_CHARACTERS)
            }
            R.id.menu_item_locations -> {
                val startFragment = LocationsFragment.newInstance(TAB_NAME_LOCATIONS)
                showTab(startFragment, TAB_NAME_LOCATIONS)
            }
            R.id.menu_item_episodes -> {
                val startFragment = EpisodesFragment.newInstance(TAB_NAME_EPISODES)
                showTab(startFragment, TAB_NAME_EPISODES)
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavigationListener()
        setDefaultSelectedTab(savedInstanceState)
    }

    private fun setNavigationListener() {
        binding.mainNavigation.setOnItemSelectedListener(navigationListener)
    }

    private fun setDefaultSelectedTab(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.mainNavigation.selectedItemId = R.id.menu_item_characters
        }
    }

    private fun showTab(startFragment: Fragment, tabName: String) {
        selectedTabName?.let {
            supportFragmentManager.saveBackStack(it)
        }
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.main_container, startFragment)
            .commit()
        supportFragmentManager.restoreBackStack(tabName)
        selectedTabName = tabName
    }

    companion object {

        private const val TAB_NAME_CHARACTERS = "tabCharacters"
        private const val TAB_NAME_LOCATIONS = "tabLocations"
        private const val TAB_NAME_EPISODES = "tabEpisodes"
    }
}
