package com.ekochkov.burnthiscalories.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.viewModel.MainActivityViewModel
import com.ekochkov.burnthiscalories.viewModel.MainFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navView: BottomNavigationView = findViewById(R.id.navigation_view)
        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        Log.d("BMTH", "activity on resume")
        viewModel.tryLaunchStepCount()
    }

    override fun onStop() {
        super.onStop()
        Log.d("BMTH", "activity on stop")
        viewModel.stopLaunchStepCount()
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}