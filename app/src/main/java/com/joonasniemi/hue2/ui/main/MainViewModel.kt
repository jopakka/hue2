package com.joonasniemi.hue2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joonasniemi.hue2.network.Hue2Repository
import com.joonasniemi.hue2.network.HueServer
import com.joonasniemi.hue2.utils.STATUS
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _bridges = MutableLiveData<Set<HueServer>>()
    val bridges: LiveData<Set<HueServer>>
        get() = _bridges

    private val _status = MutableLiveData<STATUS>()
    val status: LiveData<STATUS>
        get() = _status

    var username: String? = null
    var serverip: String? = null

    fun getBridges() {
        statusLoading()
        viewModelScope.launch {
            try {
                val response = Hue2Repository().getBridges()
                _bridges.value = response
                statusDone()
            } catch (e: Exception) {
                Log.e(TAG, "getBridges: ${e.message}")
                statusError()
            }
        }
    }

    fun registerApp(ip: String, appName: String) {
        statusLoading()
        viewModelScope.launch {
            try {
                val response = Hue2Repository().registerApp(ip, appName)
                val responseArray = (response.body() as ArrayList<*>).first() as Map<*,*>
                if(!responseArray.containsKey("error")) {
                    username = (responseArray["success"] as Map<*,*>)["username"].toString()
                    serverip = ip
                    statusDone()
                } else {
                    val info = (responseArray["error"] as Map<*,*>)["description"].toString()
                    Log.e(TAG, "registerApp info: $info")
                    statusError()
                }
            } catch (e: Exception) {
                Log.e(TAG, "registerApp: ${e.message}")
                statusError()
            }
        }
    }

    private fun statusDone() {
        _status.value = STATUS.DONE
    }

    private fun statusLoading() {
        _status.value = STATUS.LOADING
    }

    private fun statusError() {
        _status.value = STATUS.ERROR
    }
}