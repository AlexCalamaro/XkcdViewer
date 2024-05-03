package com.acalamaro.xkcdviewer.views.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.data.SettingsDataSource
import com.acalamaro.xkcdviewer.databinding.ActivityMainBinding
import com.acalamaro.xkcdviewer.views.search.SearchFragment
import com.acalamaro.xkcdviewer.views.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    @Inject lateinit var searchFragment : SearchFragment
    @Inject lateinit var settingsFragment: SettingsFragment
    @Inject lateinit var mainFragment : MainFragment
    @Inject lateinit var settingsDataSource: SettingsDataSource

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeDarkMode()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        onBackPressedDispatcher.addCallback(this) {
            if (navController.currentDestination?.id == R.id.mainFragment) {
                finish()
            } else {
                navController.popBackStack()
            }
        }
    }

    // Observe the dark mode toggle state and update the UI accordingly
    private fun observeDarkMode() {
        lifecycleScope.launch {
            settingsDataSource.getDarkModeToggleState().collect {
                // Update UI
                AppCompatDelegate.setDefaultNightMode(
                    if (it) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }

    // Observe notification toggle state and check/request notification permissions accordingly
    private fun observeNotificationToggle() {
        lifecycleScope.launch {
            settingsDataSource.getNotificationsToggleState().collect {
                if (it) {
                    // Check and request notification permissions

                }
            }
        }
    }
}