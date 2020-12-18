package com.example.instaapp

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var textMessage:TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
               textMessage.setText("Home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_search -> {
                textMessage.setText("Search")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_addpost -> {
                textMessage.setText("Post")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
              textMessage.setText("Notifications")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                textMessage.setText("Profile")
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        textMessage=findViewById<TextView>(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}