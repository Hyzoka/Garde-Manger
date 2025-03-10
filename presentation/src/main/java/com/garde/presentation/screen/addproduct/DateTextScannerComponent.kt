package com.garde.presentation.screen.addproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.garde.presentation.component.CameraView
import com.garde.presentation.utils.TextRecognitionAnalyzer

@Composable
fun DateTextScannerComponent(viewModel: AddProductViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val extractedText = remember { mutableStateOf("") }

    CameraView(
        context = context,
        lifecycleOwner = lifecycleOwner,
        analyzer = TextRecognitionAnalyzer {
            if (it != extractedText.value && it.isNotBlank()) {
                viewModel.processScannedText(it)
            }
        }
    )
}