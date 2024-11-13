package com.sinjidragon.nurijang.ui.view

import android.content.pm.PackageManager
import android.inputmethodservice.Keyboard.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.remote.api.getFacilities
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.ui.component.CurrentLocationMarker
import com.sinjidragon.nurijang.ui.component.FacilityDetail
import com.sinjidragon.nurijang.ui.theme.NurijangTheme
import com.sinjidragon.nurijang.ui.component.MoveCurrentLocationButton
import com.sinjidragon.nurijang.ui.component.PlaceMaker
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.innerShadow
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var facilityList by remember {mutableStateOf<List<Facility>>(emptyList())}
    var showBottomSheet by remember {mutableStateOf(false)}
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        )
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
                facilityList = response
            }
        }
    }
    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (hasPermission) {
            moveCurrentLocation()
        }
    }
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
        for (item in facilityList){
            val text = if (cameraPositionState.position.zoom >= 16f) {
                item.fcltyNm
            } else {
                ""
            }
            PlaceMaker(
                context = context,
                position = LatLng(item.fcltyCrdntLa, item.fcltyCrdntLo),
                text = text
                ,
                iconResourceId = R.drawable.place_maker_icon
            )
        }
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
                    .clickable { }
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
                            facilityList = response
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
                    .offset(x = (-24).dp, y = (-33).dp)
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
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
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
                            onClick = {
                                moveCamera(facility.fcltyCrdntLo,facility.fcltyCrdntLa)
                            }
                        )
                    }
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NurijangTheme {
        MapView(navController = NavController(context = LocalContext.current))
    }
}
