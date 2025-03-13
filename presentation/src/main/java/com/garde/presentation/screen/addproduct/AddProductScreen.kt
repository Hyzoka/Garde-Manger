package com.garde.presentation.screen.addproduct

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.garde.core.R
import com.garde.manger.ui.components.TopBarComponent
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

    LaunchedEffect(viewState.step) {
        if (viewState.step == AddProductStep.Saved) {
            navController.popBackStack()
        }
    }

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
            Scaffold(topBar = {
                TopBarComponent(
                    titleResId = R.string.add_product_title,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White
                        )
                    },
                    onIconClick = { navController.popBackStack() }
                )

            }) { padding ->
                Box(modifier = Modifier.padding(padding)) {
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

        }
    )
}


@Composable
fun ProductBottomSheetContent(
    viewState: AddProductViewState,
    viewModel: AddProductViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            viewState.product?.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image du produit",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Placeholder",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = viewState.product?.name ?: stringResource(R.string.unknow_name),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(
                        R.string.brand,
                        viewState.product?.brand ?: stringResource(R.string.not_available)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(
                        R.string.expiration_date,
                        viewState.expirationDate ?: stringResource(R.string.not_available)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (viewState.expirationDate == null) Color.Red else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacement avant les boutons

        // 📌 Boutons pour scanner la date ou enregistrer le produit
        if (viewState.expirationDate == null) {
            Button(
                onClick = { viewModel.startExpirationDateScan() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.scan_expiration_date))
            }
        } else {
            Button(
                onClick = { viewModel.saveProduct() },
                enabled = viewState.expirationDate.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_product))
            }
        }
    }
}

