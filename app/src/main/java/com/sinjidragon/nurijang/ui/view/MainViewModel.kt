package com.sinjidragon.nurijang.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sinjidragon.nurijang.remote.data.Facility

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _facilityList = MutableLiveData<List<Facility>>(emptyList())
    val facilityList: LiveData<List<Facility>> = _facilityList

    private val _isLaunched = MutableLiveData<Boolean>(false)
    val isLaunched : LiveData<Boolean> = _isLaunched

    fun setData(data: List<Facility>) {
        _facilityList.value = data
    }

    fun setLaunched() {
        _isLaunched.value = true
    }
}
