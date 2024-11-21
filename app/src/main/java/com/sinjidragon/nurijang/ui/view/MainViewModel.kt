package com.sinjidragon.nurijang.ui.view

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.sinjidragon.nurijang.remote.data.Facility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MainData(
    val facilityList: List<Facility> = emptyList(),
    val isLaunched: Boolean = false,
    val cameraPosition: LatLng = LatLng(37.532600,127.024612),
    val selectFacility: Facility = Facility(
        id = 0,
        distance = 0.0,
        fcltyNm = "",
        fcltyAddr = "",
        fcltyDetailAddr = "",
        rprsntvTelNo = "",
        mainItemNm = "",
        fcltyCrdntLo = 0.0,
        fcltyCrdntLa = 0.0
    )
)



@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MainData())
    val uiState = _uiState.asStateFlow()

    fun setData(data: List<Facility>) {
        _uiState.update { it.copy(facilityList = data) }
    }

    fun setLaunched() {
        _uiState.update { it.copy(isLaunched = true) }
    }

    fun setCameraPosition(la: Double, lo: Double) {
        _uiState.update { it.copy(cameraPosition = LatLng(la, lo)) }
    }

    fun setSelectFacility(facility: Facility) {
        _uiState.update { it.copy(selectFacility = facility) }
    }
}
