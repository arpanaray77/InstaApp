package com.example.instaapp

import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instaapp.Fragments.HomeFragment
import com.example.instaapp.Fragments.NotificationFragment
import com.example.instaapp.Fragments.ProfileFragment
import com.example.instaapp.Fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    internal var selectedFragment:Fragment?=null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFragment= HomeFragment()
            }
            R.id.nav_search -> {
                selectedFragment= SearchFragment()
            }
            R.id.nav_addpost -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                selectedFragment =NotificationFragment()
            }
            R.id.nav_profile -> {
               selectedFragment=ProfileFragment()
            }
        }
        if(selectedFragment!=null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment!!).commit()
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.home_toolbar))

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        //to call fragments
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
    }
}