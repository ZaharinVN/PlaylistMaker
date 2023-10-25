package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var switch: SwitchCompat
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        switch = findViewById(R.id.themeSwitcher)
        switch.isChecked = viewModel.getDarkTheme()

        switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkTheme(isChecked)
            viewModel.setAppTheme()
        }

        val backButton = findViewById<Button>(R.id.btnSettingsBack)
        backButton.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()

        switch.isChecked = viewModel.getDarkTheme()
    }


    fun onShareClick(view: View) {
        viewModel.shareApp()
    }

    fun onSupportClick(view: View) {
        viewModel.sendSupportEmail()
    }

    fun onAgreementClick(view: View) {
        viewModel.openAgreementUrl()
    }
}












