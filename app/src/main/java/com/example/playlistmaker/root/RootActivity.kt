package com.example.playlistmaker.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity(), BottomNavigationListener {
    private lateinit var binding: ActivityRootBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.playerFragment || destination.id == R.id.NewPlaylistFragment) {
                toggleBottomNavigationViewVisibility(false)
            } else {
                toggleBottomNavigationViewVisibility(true)
            }
        }
    }

    override fun toggleBottomNavigationViewVisibility(visible: Boolean) {
        bottomNavigationView.visibility = if (visible) View.VISIBLE else View.GONE
    }
}