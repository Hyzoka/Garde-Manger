package com.test.add_product.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.test.add_product.screen.AddProductStepViewState

class MultiAnalyzer(
    private val barcodeAnalyzer: BarcodeScanningAnalyzer,
    private val textAnalyzer: TextRecognitionAnalyzer,
    private val stepProvider: () -> AddProductStepViewState
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        when (stepProvider.invoke()) {
            is AddProductStepViewState.ScanProduct -> barcodeAnalyzer.analyze(image)
            is AddProductStepViewState.ScanExpirationDate -> textAnalyzer.analyze(image)
            else -> image.close()
        }
    }
}

