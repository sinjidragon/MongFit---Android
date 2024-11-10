package com.sinjidragon.nurijang.ui.view

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
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
import com.sinjidragon.nurijang.ui.theme.NurijangTheme
import com.sinjidragon.nurijang.ui.component.MoveCurrentLocationButton
import com.sinjidragon.nurijang.ui.component.PlaceMaker
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.pretendard

@Composable
fun MapView() {
    val context = LocalContext.current
    var facilityList by remember {mutableStateOf<List<Facility>>(emptyList())}
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
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 68.dp)
                .clip(RoundedCornerShape(50.dp))
                .dropShadow(offsetY = 2.dp, blur = 4.dp, color = Color.Black.copy(0.2f)),
            colors = ButtonDefaults.buttonColors(Color.White),
        ) {
            Text(
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NurijangTheme {
        MapView()
    }
}
