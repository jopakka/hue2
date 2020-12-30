package com.joonasniemi.hue2.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.joonasniemi.hue2.R
import com.joonasniemi.hue2.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.waitAndDoStuff { checkSharedPrefs() }
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }

    private fun launchSettings() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }

    private fun checkSharedPrefs() {
        val sharedPref = getSharedPreferences(getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE)
        val serverip = sharedPref.getString(getString(R.string.serverip_key), null)
        val username = sharedPref.getString(getString(R.string.username_key), null)

        if(serverip == null || username == null) {
            launchSettings()
        } else {
            launchMainActivity()
        }
    }
}