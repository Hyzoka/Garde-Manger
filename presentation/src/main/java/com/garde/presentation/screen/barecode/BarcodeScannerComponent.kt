package com.garde.presentation.screen.barecode

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.garde.presentation.component.CameraView
import com.garde.presentation.component.DrawBarcode
import com.garde.presentation.screen.addproduct.AddProductViewModel
import com.garde.presentation.utils.BarcodeScanningAnalyzer
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun BarcodeScannerComponent(viewModel: AddProductViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val detectedBarcode = remember { mutableStateListOf<Barcode>() }
    val screenWidth = remember { mutableIntStateOf(context.resources.displayMetrics.widthPixels) }
    val screenHeight = remember { mutableIntStateOf(context.resources.displayMetrics.heightPixels) }
    val imageWidth = remember { mutableIntStateOf(0) }
    val imageHeight = remember { mutableIntStateOf(0) }

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
                    viewModel.fetchProduct(barcodeValue)
                }
            }
        )
        DrawBarcode(
            barcodes = detectedBarcode,
            imageWidth = imageWidth.value,
            imageHeight = imageHeight.value,
            screenWidth = screenWidth.value,
            screenHeight = screenHeight.value
        )
    }
}
