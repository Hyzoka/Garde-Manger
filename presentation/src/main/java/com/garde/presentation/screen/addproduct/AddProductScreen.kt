package com.garde.presentation.screen.addproduct

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.garde.presentation.component.CameraView
import com.garde.presentation.component.DrawBarcode
import com.garde.presentation.utils.BarcodeScanningAnalyzer
import com.garde.presentation.utils.MultiAnalyzer
import com.garde.presentation.utils.TextRecognitionAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val detectedBarcode = remember { mutableStateListOf<Barcode>() }
    val screenWidth = remember { mutableIntStateOf(context.resources.displayMetrics.widthPixels) }
    val screenHeight = remember { mutableIntStateOf(context.resources.displayMetrics.heightPixels) }
    val imageWidth = remember { mutableIntStateOf(0) }
    val imageHeight = remember { mutableIntStateOf(0) }
    val extractedText = remember { mutableStateOf("") }
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
            Box(modifier = Modifier.fillMaxSize()) {
                // 📌 Caméra toujours active
                CameraView(
                    context = context,
                    lifecycleOwner = lifecycleOwner,
                    analyzer = MultiAnalyzer(
                        barcodeAnalyzer = BarcodeScanningAnalyzer { barcodes, width, height ->
                            detectedBarcode.clear()
                            detectedBarcode.addAll(barcodes)
                            imageWidth.intValue = width
                            imageHeight.intValue = height

                            if (barcodes.isNotEmpty()) {
                                val barcodeValue = barcodes.first().displayValue ?: ""
                                viewModel.fetchProduct(barcodeValue)
                            }
                        },
                        textAnalyzer = TextRecognitionAnalyzer { text ->
                            if (text != extractedText.value && text.isNotBlank()) {
                                viewModel.processScannedText(text)
                            }
                        },
                        stepProvider = { viewState.step }
                    )
                )

                if (viewState.step == AddProductStep.ScanBarcode)
                    DrawBarcode(
                        barcodes = detectedBarcode,
                        imageWidth = imageWidth.intValue,
                        imageHeight = imageHeight.intValue,
                        screenWidth = screenWidth.intValue,
                        screenHeight = screenHeight.intValue
                    )

                // 📌 Affichage de la BottomSheet uniquement quand elle doit être visible
                if (viewState.isBottomSheetVisible) {
                    ModalBottomSheet(
                        onDismissRequest = { /* Empêcher la fermeture manuelle */ }
                    ) {
                        ProductBottomSheetContent(viewState, viewModel)
                    }
                }
            }

        }
    )
}


@Composable
fun ProductBottomSheetContent(
    viewState: AddProductViewState,
    viewModel: AddProductViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = viewState.product?.name ?: "Nom inconnu",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Marque: ${viewState.product?.brand ?: "Non renseigné"}",
            style = MaterialTheme.typography.bodyLarge
        )

        viewState.product?.imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Image du produit",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Text(
            text = "Date de péremption: ${viewState.expirationDate ?: "Non disponible"}",
            style = MaterialTheme.typography.bodyMedium
        )

        if (viewState.expirationDate == null) {
            Button(onClick = { viewModel.startExpirationDateScan() }) {
                Text("Scanner la date de péremption")
            }
        } else {
            Button(
                onClick = { viewModel.saveProduct() },
                enabled = viewState.expirationDate.isNotEmpty()
            ) {
                Text("Enregistrer le produit")
            }
        }
    }
}


