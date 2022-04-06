package com.ubaya.uts_advnative_160419072

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.ubaya.uts_advnative_160419072.databinding.ActivityMainBinding
import com.ubaya.uts_advnative_160419072.models.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_login,
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_profile
            )
        )

        val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)

        if (checkLogin()) {
            navGraph.setStartDestination(R.id.navigation_home)
        } else {
            navGraph.setStartDestination(R.id.navigation_login)
        }

        navController.graph = navGraph

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> navView.visibility = View.VISIBLE
                R.id.navigation_search -> navView.visibility = View.VISIBLE
                R.id.navigation_profile -> navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }

        navView.setOnItemReselectedListener { }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun checkLogin(): Boolean {
        val prefs = getSharedPreferences("app", Context.MODE_PRIVATE)

        val userString = prefs.getString("user", "")

        return if (userString !== "") {
            Globals.user = Gson().fromJson(userString, User::class.java)

            true
        } else {
            false
        }
    }
}