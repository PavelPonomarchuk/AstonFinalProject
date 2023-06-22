package ru.ponomarchukpn.astonfinalproject.presentation.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        setupSplashScreen()
        setContentView(binding.root)
        setNavigationListener()
        setDefaultSelectedTab(savedInstanceState)
    }

    private fun setupSplashScreen() {
        var keepSplashScreenOn = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(SPLASH_SCREEN_TIMEOUT)
                keepSplashScreenOn = false
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
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
        private const val SPLASH_SCREEN_TIMEOUT = 3000L
    }
}
