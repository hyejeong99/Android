package com.example.guru_oneulmaru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {//bottomnavigation 클릭 시 해당 프래그먼트로 이동하기
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        //joinActivity에서 email값 받아오기
        val loginIntent = intent
        val email=loginIntent.getStringExtra("email")
        //change LoginActivity에서 값 받아오기, homeFragment로 넘어가기
        val bundle= bundleOf("email" to email)
        navController.navigate(R.id.navigation_home, bundle)

        supportActionBar?.hide()
    }
}
