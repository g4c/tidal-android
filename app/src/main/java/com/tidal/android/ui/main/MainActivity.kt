package com.tidal.android.ui.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tidal.android.R
import com.tidal.android.databinding.ActivityMainBinding
import com.tidal.android.permission.PermissionManager
import com.tidal.android.ui.downloads.DownloadsFragment
import com.tidal.android.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionManager: PermissionManager
    private val searchFragment = SearchFragment()
    private val downloadsFragment = DownloadsFragment()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            Toast.makeText(
                this,
                "Permissions granted",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionManager = PermissionManager(this)
        checkAndRequestPermissions()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(searchFragment)
        }
    }

    private fun checkAndRequestPermissions() {
        if (!permissionManager.hasAllRequiredPermissions()) {
            if (shouldShowRationaleDialog()) {
                showPermissionRationaleDialog()
            } else {
                requestPermissions()
            }
        }
    }

    private fun shouldShowRationaleDialog(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            false
        }
    }

    private fun showPermissionRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permissions Required")
            .setMessage("This app needs access to your storage and internet to work properly.")
            .setPositiveButton("Grant Permissions") { _, _ ->
                requestPermissions()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(permissionManager.getRequiredPermissions())
    }

    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permissions Denied")
            .setMessage("Some permissions were denied. The app may not work properly.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    loadFragment(searchFragment)
                    true
                }
                R.id.nav_downloads -> {
                    loadFragment(downloadsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commitNow()
    }
}