package com.joonasniemi.hue2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joonasniemi.hue2.network.Hue2UpnpRepository
import com.joonasniemi.hue2.network.HueServer
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _bridges = MutableLiveData<Set<HueServer>>()
    val bridges: LiveData<Set<HueServer>>
        get() = _bridges

    fun getBridges() {
        viewModelScope.launch {
            try {
                val response = Hue2UpnpRepository().getBridges()
                Log.i(TAG, response.toString())
                _bridges.value = response
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
    }
}