package com.sinjidragon.nurijang.ui.view

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.google.maps.android.compose.rememberCameraPositionState
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.ui.component.BaseAlert
import com.sinjidragon.nurijang.ui.component.CustomClustering
import com.sinjidragon.nurijang.ui.component.FacilityDetail
import com.sinjidragon.nurijang.ui.component.MoveCurrentLocationButton
import com.sinjidragon.nurijang.ui.nav.NavGroup
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.innerShadow
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(navController: NavController, viewModel: MainViewModel) {
    val uiState by viewModel.uiState. collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectFacilityState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val facilityListState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()



    viewModel.setPermission(
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
    )
    LaunchedEffect(Unit) {
        println(uiState.facilityList)
    }
    LaunchedEffect(viewModel) {
        Log.d("jalbwa","${viewModel.uiEffect}")
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                MainSideEffect.Failed -> {
                    Log.d("jalbwa","failed")
                    viewModel.setIsError(true)
                }
            }
        }
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.setPermission(isGranted)
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

    suspend fun moveCamera(lo: Double, la: Double) {
        cameraPositionState.animate(CameraUpdateFactory.newLatLng(LatLng(la,lo)),1000)
    }
    LaunchedEffect(currentLocation.value) {
        currentLocation.value?.let { location ->
            viewModel.getFacilities(location.longitude,location.latitude)
        }
    }
    LaunchedEffect(Unit) {
        if (!uiState.isLaunched) {
            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (uiState.hasPermission) {
                moveCurrentLocation()
            }
            viewModel.setLaunched()
        }
    }
    LaunchedEffect(currentBackStackEntry) {
        if (currentBackStackEntry?.destination?.route == "map") {
            if (uiState.isSelected){
                println("${uiState.selectFacility.fcltyCrdntLo} ${uiState.selectFacility.fcltyCrdntLa}")
                moveCamera(uiState.selectFacility.fcltyCrdntLo, uiState.selectFacility.fcltyCrdntLa)
            }
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
        ),
    ) {
        CustomClustering(
            items = uiState.facilityList,
            onItemClick = { item ->
                viewModel.setSelectFacility(item)
                coroutineScope.launch {
                    moveCamera(item.fcltyCrdntLo, item.fcltyCrdntLa)
                }
                viewModel.setIsSelected(true)
                true
            },
            clusterItemContent = {
                Box {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(16.dp, 22.dp),
                        painter = painterResource(R.drawable.place_maker_icon),
                        contentDescription = ""
                    )
                }
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        AnimatedVisibility(
            visible = uiState.isError
        ) {
            BaseAlert(
                contentText = uiState.errorText,
                onDismissRequest = {
                    viewModel.setIsError(false)
                },
                onButtonClick = {viewModel.setIsError(false)}
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(44.dp)
                .align(Alignment.TopCenter),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        viewModel.setCameraPosition(
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
            ) {
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
                        navController.navigate(NavGroup.CHAT_BOT)
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
                    painter = painterResource(id = R.drawable.bot_icon),
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
                        viewModel.setCameraPosition(
                            cameraPositionState.position.target.longitude,
                            cameraPositionState.position.target.latitude
                        )
                        viewModel.getFacilities(
                            cameraPositionState.position.target.longitude,
                            cameraPositionState.position.target.latitude
                        )
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
        if (uiState.hasPermission) {
            MoveCurrentLocationButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-24).dp, y = (-81).dp),
                onClick = { moveCurrentLocation() }
            )
        }
        if (uiState.showBottomSheet) {
            ModalBottomSheet(
                sheetState = facilityListState,
                modifier = Modifier
                    .height(650.dp)
                    .innerShadow()
                    .align(Alignment.BottomCenter),
                onDismissRequest = {
                    viewModel.setShowBottomSheet(false)
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                scrimColor = Color.Transparent
            ) {
                if (uiState.facilityList.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(uiState.facilityList) { facility ->
                            FacilityDetail(
                                modifier = Modifier,
                                facilityName = facility.fcltyNm,
                                eventName = facility.mainItemNm,
                                facilityAddress = facility.fcltyAddr,
                                facilityDetailAddress = facility.fcltyDetailAddr,
                                tellNumber = facility.rprsntvTelNo,
                                distance = facility.distance,
                                onClick = {
                                    coroutineScope.launch {
                                        moveCamera(facility.fcltyCrdntLo, facility.fcltyCrdntLa)
                                    }
                                }
                            )
                        }
                    }
                }
                else {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center,
                        text = "결과가 없습니다",
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = gray2
                    )
                }
            }
        }
        if (uiState.isSelected) {
            ModalBottomSheet(
                sheetState = selectFacilityState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(230.dp)
                    .innerShadow(),
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                scrimColor = Color.Transparent,
                onDismissRequest = {
                    viewModel.setIsSelected(false)
                }
            ) {
                FacilityDetail(
                    modifier = Modifier,
                    facilityName = uiState.selectFacility.fcltyNm,
                    tellNumber = uiState.selectFacility.rprsntvTelNo,
                    eventName = uiState.selectFacility.mainItemNm,
                    distance = uiState.selectFacility.distance,
                    facilityAddress = uiState.selectFacility.fcltyAddr,
                    facilityDetailAddress = uiState.selectFacility.fcltyDetailAddr,
                    isButton = false
                )
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
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 18.dp)
                    .clickable {
                        viewModel.setShowBottomSheet(true)
                    },
                text = "목록 표시"
            )
        }
    }
}
