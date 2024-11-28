package com.sinjidragon.nurijang.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.GetFacilitiesRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    ),
    val showBottomSheet: Boolean = false,
    val hasPermission: Boolean = false,
    val isSelected: Boolean = false,
    val currentLocation : LatLng = LatLng(37.532600,127.024612)
)

sealed interface MainSideEffect {
    data object Failed : MainSideEffect
}


class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainData())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MainSideEffect>()
    val uiEffect: SharedFlow<MainSideEffect> = _uiEffect.asSharedFlow()

    fun setData(data: List<Facility>) {
        _uiState.update { it.copy(facilityList = data) }
    }

    fun setPermission(data: Boolean) {
        _uiState.update { it.copy(hasPermission = data) }
    }

    fun setShowBottomSheet(show: Boolean) {
        _uiState.update { it.copy(showBottomSheet = show) }
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
    fun setIsSelected(isSelected: Boolean) {
        _uiState.update { it.copy(isSelected = isSelected) }
    }

    fun getFacilities(Lo: Double, La: Double) {
        viewModelScope.launch {
            try {
                val facilityService = Client.facilityService
                val request = GetFacilitiesRequest(Lo,La)
                val response = facilityService.getFacilities(request)
                setData(response)
            } catch (e: HttpException) {
                _uiEffect.emit(MainSideEffect.Failed)
            }
        }
    }
}
