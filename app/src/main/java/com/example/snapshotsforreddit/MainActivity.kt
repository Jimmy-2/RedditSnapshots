package com.example.snapshotsforreddit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        setupBottomNavMenu(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.subscribedFragment,R.id.inboxFragment, R.id.accountOverviewFragment, R.id.downloadedPostsFragment,  R.id.optionsFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        bottomNavigationBar.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}