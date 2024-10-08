package com.squidink.xkcdviewer.views.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
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
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.data.SettingsDataSource
import com.squidink.xkcdviewer.databinding.ActivityMainBinding
import com.squidink.xkcdviewer.extensions.showErrorDialog
import com.squidink.xkcdviewer.extensions.showNotificationRequestDialog
import com.squidink.xkcdviewer.extensions.showOptionalIcons
import com.squidink.xkcdviewer.notifications.XkcdNotificationPermissions
import com.squidink.xkcdviewer.notifications.workmanager.CheckLatestWorker
import com.squidink.xkcdviewer.views.search.SearchFragment
import com.squidink.xkcdviewer.views.settings.SettingsFragment
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

    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeDarkMode()
        observeNotificationToggle()
        checkHandleFirstLaunch()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        createWorker()
                    } else if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        lifecycleScope.launch {
                            settingsDataSource.setNotificationsToggleState(false)
                            showErrorDialog(
                                getString(R.string.notification_error_title),
                                getString(R.string.notification_error_body)
                            )
                        }
                    }
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

    // Check for, and handle first time launch events
    private fun checkHandleFirstLaunch() {
        lifecycleScope.launch {
            if(settingsDataSource.getIsFirstAppLaunch() &&
                !XkcdNotificationPermissions.checkPermissions(this@MainActivity)) {
                showNotificationRequestDialog(settingsDataSource)
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

    // Inflate the menu and show optional icons in dropdown
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.showOptionalIcons()
        return true
    }
}