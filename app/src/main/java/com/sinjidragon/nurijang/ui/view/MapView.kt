package com.sinjidragon.nurijang.ui.view

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.remote.api.getFacilities
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.ui.component.CurrentLocationMarker
import com.sinjidragon.nurijang.ui.component.FacilityDetail
import com.sinjidragon.nurijang.ui.component.MoveCurrentLocationButton
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.innerShadow
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import com.sinjidragon.semtong.nav.NavGroup
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun MapView(navController: NavController,mainViewModel: MainViewModel) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isLaunched = mainViewModel.isLaunched.value ?:false
    val selectFacility = mainViewModel.selectFacility.value
    var isSelected by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val facilityList = remember { mutableStateListOf<Facility>() }
    var showBottomSheet by remember {mutableStateOf(false)}
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    LaunchedEffect(mainViewModel.facilityList) {
        mainViewModel.facilityList.observeForever { newList->
            facilityList.clear()
            facilityList.addAll(newList)
        }
        println(facilityList)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val currentLocation = remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()
    fun moveCurrentLocation() {
        println(fusedLocationClient.lastLocation)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                currentLocation.value = latLng
                currentLocation.value?.let { nonNullLocation ->
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(nonNullLocation, 16f)
                }
            }
        }
    }
    fun moveCamera(lo:Double,la:Double){
        cameraPositionState.move(
            CameraUpdateFactory.newLatLng(LatLng(la,lo))
        )
    }
    LaunchedEffect(currentLocation.value) {
        currentLocation.value?.let { location ->
            val response = getFacilities(location.longitude, location.latitude)
            if (response != null) {
                mainViewModel.setData(response)
            }
        }
    }
    LaunchedEffect(Unit) {
        Log.d("adfs","$isLaunched")
        if (!isLaunched) {
            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (hasPermission) {
                moveCurrentLocation()
            }
            mainViewModel.setLaunched()
        }
    }
    LaunchedEffect(currentBackStackEntry) {
        if (currentBackStackEntry?.destination?.route == "map") {
            if (selectFacility != null) {
                moveCamera(selectFacility.fcltyCrdntLo,selectFacility.fcltyCrdntLa)
                isSelected = true
            }
        }
    }
    Log.d("도영이바보", "MapView")
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(),
        uiSettings = MapUiSettings(zoomControlsEnabled = false),
    ) {
        currentLocation.value?.let { location ->
            CurrentLocationMarker(
                context = context,
                position = location,
                iconResourceId = R.drawable.now_location_icon
            )
        }
        Clustering(
            items = facilityList,
            onClusterItemClick = { item ->
                mainViewModel.setSelectFacility(item)
                moveCamera(item.fcltyCrdntLo,item.fcltyCrdntLa)
                isSelected = true
                true
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(44.dp)
                .align(Alignment.TopCenter),
        ){
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        mainViewModel.setCameraPosition(
                            cameraPositionState.position.target.latitude,
                            cameraPositionState.position.target.longitude
                        )
                        navController.navigate(NavGroup.SEARCH)
                    }
                    .weight(1f)
                    .dropShadow()
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.Start,
            ){
                Spacer(modifier = Modifier.width(11.dp))
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = "시설명 종목",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = gray2
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clickable {
                        TODO("일단 보류기능")
                    }
                    .clip(RoundedCornerShape(8.dp))
                    .dropShadow()
                    .background(mainColor)
                    .width(40.dp)
                    .fillMaxHeight(),
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.mike_icon),
                    contentDescription = ""
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 68.dp)
                .size(113.dp, 30.dp)
                .dropShadow(
                    offsetY = 2.dp,
                    blur = 4.dp,
                    color = Color.Black.copy(0.2f),
                    shape = RoundedCornerShape(50.dp)
                )
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .clickable {
                    coroutineScope.launch {
                        val response = getFacilities(
                            fcltyCrdntLa = cameraPositionState.position.target.latitude,
                            fcltyCrdntLo = cameraPositionState.position.target.longitude
                        )
                        if (response != null) {
                            mainViewModel.setData(response)
                        }
                    }
                },
        ) {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "이 지역 검색",
                color = Color.Black,
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
        if (hasPermission) {
            MoveCurrentLocationButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-24).dp, y = (-81).dp)
                ,
                onClick = { moveCurrentLocation() }
            )
        }
        if (showBottomSheet){
            ModalBottomSheet(
                modifier = Modifier
                    .height(650.dp)
                    .innerShadow(),
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                scrimColor = Color.Transparent
            ) {
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ){
                    items(facilityList){facility ->
                        FacilityDetail(
                            modifier = Modifier,
                            facilityName = facility.fcltyNm,
                            eventName = facility.mainItemNm,
                            facilityAddress = facility.fcltyAddr,
                            facilityDetailAddress = facility.fcltyDetailAddr,
                            tellNumber = facility.rprsntvTelNo,
                            distance = facility.distance,
                            onClick = {
                                moveCamera(facility.fcltyCrdntLo,facility.fcltyCrdntLa)
                            }
                        )
                    }
                }
            }
        }
        if (isSelected){
            ModalBottomSheet(
                modifier = Modifier
                    .height(230.dp)
                    .innerShadow()
                    .align(Alignment.BottomCenter),
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                scrimColor = Color.Transparent,
                onDismissRequest = {
                    isSelected = false
                }
            ) {
                if (selectFacility != null) {
                    FacilityDetail(
                        modifier = Modifier,
                        facilityName = selectFacility.fcltyNm,
                        eventName = selectFacility.mainItemNm,
                        distance = selectFacility.distance,
                        facilityAddress = selectFacility.fcltyAddr,
                        facilityDetailAddress = selectFacility.fcltyDetailAddr
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(77.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .innerShadow()
        ){
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 18.dp)
                    .clickable { showBottomSheet = true },
                text = "목록 표시"
            )
        }
    }
}
