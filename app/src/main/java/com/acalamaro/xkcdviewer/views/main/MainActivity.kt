package com.acalamaro.xkcdviewer.views.main

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.data.SettingsDataSource
import com.acalamaro.xkcdviewer.databinding.ActivityMainBinding
import com.acalamaro.xkcdviewer.notifications.XkcdNotificationPermissions
import com.acalamaro.xkcdviewer.notifications.workmanager.CheckLatestWorker
import com.acalamaro.xkcdviewer.views.search.SearchFragment
import com.acalamaro.xkcdviewer.views.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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
        observeNotificationToggle()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            XkcdNotificationPermissions.PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createWorker()
                }
            }
        }
    }

    // Observe the dark mode toggle state and update the UI accordingly
    private fun observeDarkMode() {
        lifecycleScope.launch {
            settingsDataSource.getDarkModeToggleState().collect { allowDarkMode ->
                // Update UI
                AppCompatDelegate.setDefaultNightMode(
                    if (allowDarkMode) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }

    // Observe notification toggle state and check/request notification permissions accordingly
    private fun observeNotificationToggle() {
        lifecycleScope.launch {
            settingsDataSource.getNotificationsToggleState().collect { enabled ->
                if (enabled) {
                    if(XkcdNotificationPermissions.checkRequestPermissions(this@MainActivity)) {
                        createWorker()
                    }
                } else {
                    cancelWorker()
                }
            }
        }
    }

    private fun createWorker() {
        val workManager = WorkManager.getInstance(applicationContext)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()
        val work = PeriodicWorkRequestBuilder<CheckLatestWorker>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            getString(R.string.notification_worker_name),
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            work
        )
    }

    private fun cancelWorker() {
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.cancelUniqueWork(getString(R.string.notification_worker_name))
    }
}