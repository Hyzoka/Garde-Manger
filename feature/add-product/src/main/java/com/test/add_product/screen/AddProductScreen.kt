package com.test.add_product.screen

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.garde.component.MessageView
import com.garde.component.TopBarComponent
import com.garde.component.textfield.ExpirationDateTextField
import com.garde.core.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import com.test.add_product.component.CameraView
import com.test.add_product.component.DrawBarcode
import com.test.add_product.utils.BarcodeScanningAnalyzer
import com.test.add_product.utils.MultiAnalyzer
import com.test.add_product.utils.TextRecognitionAnalyzer

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddProductScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val step by viewModel.step.collectAsStateWithLifecycle()

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

    LaunchedEffect(Unit) {
        viewModel.closeScreen.collect {
            onPopBackStack()
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBarComponent { onPopBackStack() }

                    Spacer(Modifier.weight(1f))

                    MessageView(
                        lottie = R.raw.camera_permission,
                        message = R.string.enable_permission,
                        onClicked = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
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
                    onIconClick = { onPopBackStack() }
                )

            }) { padding ->
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(padding)
                ) {
                    CameraView(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        analyzer = MultiAnalyzer(
                            barcodeAnalyzer = BarcodeScanningAnalyzer { barcodes, width, height ->
                                detectedBarcode.clear()
                                detectedBarcode.addAll(barcodes)
                                imageWidth.intValue = width
                                imageHeight.intValue = height

                                if (barcodes.isNotEmpty() && step == AddProductStepViewState.ScanProduct) {
                                    val barcodeValue = barcodes.first().displayValue ?: ""
                                    viewModel.fetchProduct(barcodeValue)
                                }
                            },
                            textAnalyzer = TextRecognitionAnalyzer { text ->
                                if (text != extractedText.value && text.isNotBlank() && step == AddProductStepViewState.ScanExpirationDate) {
                                    viewModel.handleExpirationDate(text)
                                }
                            },
                            stepProvider = { step }
                        )
                    )

                    when (step) {
                        AddProductStepViewState.ScanProduct -> {
                            DrawBarcode(
                                barcodes = detectedBarcode,
                                imageWidth = imageWidth.intValue,
                                imageHeight = imageHeight.intValue,
                                screenWidth = screenWidth.intValue,
                                screenHeight = screenHeight.intValue
                            )
                        }

                        AddProductStepViewState.SelectQuantity -> DisplayModalProductBottomSheet(
                            productState,
                            step,
                            viewModel
                        )


                        AddProductStepViewState.ScanExpirationDate -> {
                            if (productState.showManualExpirationDateInput) {
                                ExpirationDateInputBottomSheet(
                                    onErrorDateState = productState.errorDateFormatMessage,
                                    onConfirm = { date -> viewModel.handleExpirationDate(date) },
                                    onRetry = { viewModel.retryExpirationScan() }
                                )
                            }
                        }

                        AddProductStepViewState.ConfirmProduct -> DisplayModalProductBottomSheet(
                            productState,
                            step,
                            viewModel
                        )

                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayModalProductBottomSheet(
    productState: AddProductViewState,
    step: AddProductStepViewState,
    viewModel: AddProductViewModel
) {
    ModalBottomSheet(onDismissRequest = { viewModel.resetProduct() }) {
        ProductBottomSheetContent(
            productState,
            step,
            viewModel
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductBottomSheetContent(
    productState: AddProductViewState,
    step: AddProductStepViewState,
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
                model = productState.product?.imageUrl,
                contentDescription = "Image du produit",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productState.product?.name ?: stringResource(R.string.unknow_name),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(
                        R.string.brand,
                        productState.product?.brand ?: stringResource(R.string.not_available)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(
                        R.string.expiration_date,
                        productState.product?.expirationDate
                            ?: stringResource(R.string.not_available)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (productState.product?.expirationDate == null) Color.Red else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (step) {
            AddProductStepViewState.ScanProduct -> {
                // Nothing to do
            }


            AddProductStepViewState.SelectQuantity -> {
                var quantity by rememberSaveable { mutableStateOf("1") }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.quantity),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { newValue ->
                            quantity = newValue
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = productState.isQuantityError,
                        supportingText = {
                            if (productState.isQuantityError) {
                                Text(
                                    stringResource(R.string.please_enter_valid_quantity),
                                    color = Color.Red
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espacement avant les boutons

                Button(
                    onClick = {
                        viewModel.updateQuantity(quantity)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.scan_expiration_date))
                }
            }

            AddProductStepViewState.ScanExpirationDate -> {
                // Nothing to do
            }

            AddProductStepViewState.ConfirmProduct -> {
                var quantity by rememberSaveable {
                    mutableStateOf(
                        productState.product?.quantity?.toString().orEmpty()
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.quantity),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { newValue ->
                            quantity = newValue
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = productState.isQuantityError,
                        supportingText = {
                            if (productState.isQuantityError) {
                                Text("Veuillez entrer une quantité valide", color = Color.Red)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espacement avant les boutons

                Button(
                    onClick = { viewModel.updateQuantity(quantity, true) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_product))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpirationDateInputBottomSheet(
    onErrorDateState: Int?,
    onConfirm: (String) -> Unit,
    onRetry: () -> Unit
) {
    var dateInput by rememberSaveable { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = { }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.manual_expiration_entry_title),
                style = MaterialTheme.typography.titleMedium
            )

            ExpirationDateTextField(dateInput, { dateInput = it }, onErrorDateState)

            Button(
                onClick = { onConfirm(dateInput) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.confirm))
            }

            TextButton(
                onClick = onRetry,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.retry_scan))
            }
        }
    }
}


