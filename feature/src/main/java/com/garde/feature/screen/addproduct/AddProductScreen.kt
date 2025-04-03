package com.garde.feature.screen.addproduct

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.garde.core.R
import com.garde.feature.component.MessageView
import com.garde.feature.component.TopBarComponent
import com.garde.feature.component.scan.CameraView
import com.garde.feature.component.scan.DrawBarcode
import com.garde.feature.utils.BarcodeScanningAnalyzer
import com.garde.feature.utils.MultiAnalyzer
import com.garde.feature.utils.TextRecognitionAnalyzer
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

    LaunchedEffect(Unit) {
        viewModel.validateForm()
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBarComponent(){ navController.popBackStack() }

                    Spacer(Modifier.weight(1f))

                    MessageView(
                        lottie = R.raw.camera_permission,
                        message = R.string.enable_permission,
                        onClicked = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            startActivity(context, intent, null)
                        }
                    )
                }
            }
        },
        content = {
            Scaffold(topBar = {
                TopBarComponent(
                    titleResId = R.string.add_product_title,
                    onIconClick = { navController.popBackStack() }
                )

            }) { padding ->
                Box(modifier = Modifier.padding(padding)) {
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


@OptIn(ExperimentalGlideComposeApi::class)
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
            GlideImage(
                model = viewState.product?.imageUrl,
                contentDescription = "Image du produit",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Quantité :", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = viewState.quantity,
                onValueChange = { newValue ->
                    viewModel.updateQuantity(newValue)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = viewState.isQuantityError,
                supportingText = {
                    if (viewState.isQuantityError) {
                        Text("Veuillez entrer une quantité valide", color = Color.Red)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
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
                enabled = viewState.isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_product))
            }
        }
    }
}

