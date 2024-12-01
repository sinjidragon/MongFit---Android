package com.sinjidragon.nurijang.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.FacilityLite
import com.sinjidragon.nurijang.remote.data.GetFacilitiesRequest
import com.sinjidragon.nurijang.remote.data.SearchRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
    val currentLocation : LatLng = LatLng(37.532600,127.024612),
    val eventList: List<String> = emptyList(),
    val searchFacilityList: List<FacilityLite> = emptyList(),
    val searchText: String = ""
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
    fun setEventList(eventList: List<String>){
        _uiState.update { it.copy(eventList = eventList) }
    }
    fun setSearchFacilityList(searchFacilityList: List<FacilityLite>){
        _uiState.update { it.copy(searchFacilityList = searchFacilityList) }
    }
    fun setSearchText(searchText: String){
        _uiState.update { it.copy(searchText = searchText) }
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
    fun suggestions(lo: Double,la: Double,text: String){
        viewModelScope.launch {
            try {
                val searchService = Client.searchService
                val request = SearchRequest(lo,la,text)
                val response = searchService.suggestions(request)
                setEventList(response.mainItems)
                setSearchFacilityList(response.facilities)
            } catch (e: HttpException) {
                _uiEffect.emit(MainSideEffect.Failed)
            }
        }
    }
    fun eventSearch(lo: Double,la: Double,text: String){
        viewModelScope.launch {
            try {
                val searchService = Client.searchService
                val request = SearchRequest(lo, la, text)
                val response = searchService.eventSearch(request)
                setData(response)
            } catch (e: HttpException) {
                _uiEffect.emit(MainSideEffect.Failed)
            }
        }
    }
}
