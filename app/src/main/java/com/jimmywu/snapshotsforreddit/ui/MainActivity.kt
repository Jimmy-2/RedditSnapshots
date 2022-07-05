package com.jimmywu.snapshotsforreddit.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.util.updateForTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateForTheme(viewModel.getSelectedTheme())

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
//        NavigationUI.setupActionBarWithNavController(this, navController)

        setupBottomNavMenu(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.subscribedFragment,
                R.id.inboxFragment,
                R.id.accountOverviewFragment,
                R.id.searchFragment,
                R.id.settingsFragment
            )
        )

//        setupActionBarWithNavController(navController, appBarConfiguration)


//
//        bottomNavigationBar.setOnItemReselectedListener{ item ->
//
//        }

        //remove elevation shadow from the action bar
        //supportActionBar?.elevation = 0F
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedTheme.collect { theme ->
                        updateForTheme(theme)
                    }
                }
            }
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        bottomNavigationBar.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}
