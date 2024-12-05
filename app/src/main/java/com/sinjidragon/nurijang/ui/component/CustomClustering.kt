package com.sinjidragon.nurijang.ui.component

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun <T : ClusterItem>CustomClustering(
    items: Collection<T>,
    onItemClick : (T) -> Boolean = { false },
    clusterItemContent: @[UiComposable Composable] ((T) -> Unit)? = null,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val clusterManager = rememberClusterManager<T>() ?: return

    clusterManager.setAlgorithm(
        NonHierarchicalViewBasedAlgorithm(
            screenWidth.value.toInt(),
            screenHeight.value.toInt()
        )
    )
    val renderer = rememberClusterRenderer(
        clusterContent = { cluster ->
            CircleContent(
                modifier = Modifier.size(40.dp),
                text = "%,d".format(cluster.size),
            )
        },
        clusterItemContent = clusterItemContent,
        clusterManager = clusterManager,
    )
    SideEffect {
        clusterManager.setOnClusterClickListener {
            Log.d(TAG, "Cluster clicked! $it")
            false
        }
        clusterManager.setOnClusterItemClickListener(onItemClick)
        clusterManager.setOnClusterItemInfoWindowClickListener {
            Log.d(TAG, "Cluster item info window clicked! $it")
        }
    }
    SideEffect {
        if (clusterManager.renderer != renderer) {
            clusterManager.renderer = renderer ?: return@SideEffect
        }
    }

    Clustering(
        items = items,
        clusterManager = clusterManager,
    )
}

@Composable
private fun CircleContent(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier,
        shape = CircleShape,
        color = mainColor,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text,
                fontSize = 16.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}
