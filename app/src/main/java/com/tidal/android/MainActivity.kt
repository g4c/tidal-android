package com.tidal.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tidal.android.ui.downloads.DownloadsFragment
import com.tidal.android.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private val searchFragment = SearchFragment()
    private val downloadsFragment = DownloadsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fragmentContainer = findViewById<android.widget.FrameLayout>(R.id.fragment_container)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit()
        }

        // Handle bottom navigation item selection
        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_search -> searchFragment
                R.id.navigation_downloads -> downloadsFragment
                else -> searchFragment
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true
        }
    }
}