package com.garde.presentation.screen.barecode

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.garde.domain.model.Product
import com.garde.presentation.component.CameraView
import com.garde.presentation.utils.BarcodeScanningAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeScanningScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)

    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            LaunchedEffect(Unit) {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            Column {
                Toast.makeText(context, "Permission denied.", Toast.LENGTH_LONG).show()
            }
        },
        content = {
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanSurface(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val detectedBarcode = remember { mutableStateListOf<Barcode>() }
    val screenWidth = remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
    val screenHeight = remember { mutableStateOf(context.resources.displayMetrics.heightPixels) }
    val imageWidth = remember { mutableStateOf(0) }
    val imageHeight = remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState()

    Box(modifier = Modifier.fillMaxSize()) {
        CameraView(
            context = context,
            lifecycleOwner = lifecycleOwner,
            analyzer = BarcodeScanningAnalyzer { barcodes, width, height ->
                detectedBarcode.clear()
                detectedBarcode.addAll(barcodes)
                imageWidth.value = width
                imageHeight.value = height

                if (barcodes.isNotEmpty()) {
                    val barcodeValue = barcodes.first().displayValue ?: ""
                }
            }
        )

    }
}

@Composable
fun ProductDetails(product: Product) {
    Timber.i("AZEAZE ${product.imageUrl}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = product.name ?: "Nom inconnu", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Marque: ${product.brand ?: "Non renseigné"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Date de péremption: ${product.expirationDate ?: "Non disponible"}",
            style = MaterialTheme.typography.bodyMedium
        )

        AsyncImage(
            model = product.imageUrl,
            contentDescription = "Image du produit",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
