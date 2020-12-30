package com.joonasniemi.hue2.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joonasniemi.hue2.utils.STATUS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private var counter = 0

    private val _status = MutableLiveData<STATUS>()
    val status: LiveData<STATUS>
        get() = _status

    fun waitAndDoStuff(function: () -> Unit) {
        _status.value = STATUS.LOADING
        viewModelScope.launch {
            while (counter < 100) {
                counter += 25
                delay(1000)
            }
            _status.value = STATUS.DONE
            function()
        }
    }
}