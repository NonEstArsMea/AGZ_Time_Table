package com.NonEstArsMea.agz_time_table.present.audWorkloadFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.Provides
import javax.inject.Inject

class AudWorkloadViewModel @Inject constructor() : ViewModel() {

    private var _position = MutableLiveData(0)
    val position: LiveData<Int>
        get() = _position

    fun setPosition(p: Int) {
        _position.value = p
    }

}