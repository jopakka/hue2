package com.joonasniemi.hue2.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.joonasniemi.hue2.R
import com.joonasniemi.hue2.databinding.ActivityMainBinding
import com.joonasniemi.hue2.utils.STATUS

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setContentView(binding.root)

        getSharedPrefs()
        checkPrefs()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.testButton.setOnClickListener {
            Log.i(TAG, "Button pressed")
            viewModel.bridges.value?.let {
                val ip = it.first().ip
                viewModel.registerApp(ip, "hue2")
                waitUsername()
            }
        }
        binding.test2Button.setOnClickListener {
            Log.i(TAG, "Button2 pressed")
            Log.i(TAG, getSharedPreferences(getString(R.string.shared_prefs_key),
                    Context.MODE_PRIVATE).getString(getString(R.string.username_key),
                    "No username").toString())
            Log.i(TAG, getSharedPreferences(getString(R.string.shared_prefs_key),
                    Context.MODE_PRIVATE).getString(getString(R.string.serverip_key),
                    "No serverip").toString())
        }
    }

    private fun waitUsername() {
        viewModel.status.observe(this) {
            if(it == STATUS.DONE) {
                viewModel.status.removeObservers(this)
                viewModel.username?.let { username ->
                    saveString(getString(R.string.username_key), username)
                }
                viewModel.serverip?.let { serverip ->
                    saveString(getString(R.string.serverip_key), serverip)
                }
            } else if (it == STATUS.ERROR) {
                viewModel.status.removeObservers(this)
            }
        }
    }

    private fun saveString(key: String, value: String) {
        val sharedPref = getSharedPreferences(getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getSharedPrefs() {
        val sharedPref = getSharedPreferences(getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE)
        viewModel.serverip = sharedPref.getString(getString(R.string.serverip_key), null)
        viewModel.username = sharedPref.getString(getString(R.string.username_key), null)
    }

    private fun checkPrefs() {
        if(viewModel.serverip == null || viewModel.username == null) {
            viewModel.getBridges()
        }
    }
}