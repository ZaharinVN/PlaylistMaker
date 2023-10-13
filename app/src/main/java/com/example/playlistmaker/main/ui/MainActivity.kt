package com.example.playlistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.LibraryActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupButtons()
        Companion.observeDarkMode(this)
    }

    private fun setupButtons() {
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val btnLibrary = findViewById<Button>(R.id.btnLibrary)
        btnLibrary.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private fun observeDarkMode(mainActivity: MainActivity) {
            val sharedPrefs = mainActivity.getSharedPreferences("settings", MODE_PRIVATE)
            val darkMode = sharedPrefs.getBoolean("darkTheme", false)
            mainActivity.viewModel.setDarkModeEnabled(darkMode)

            mainActivity.viewModel.darkModeEnabled.observe(mainActivity) { enabled ->
                if (enabled) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}








