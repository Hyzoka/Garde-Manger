package com.test.add_product.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.test.add_product.screen.AddProductStep

class MultiAnalyzer(
    private val barcodeAnalyzer: BarcodeScanningAnalyzer,
    private val textAnalyzer: TextRecognitionAnalyzer,
    private val stepProvider: () -> AddProductStep
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        when (stepProvider.invoke()) {
            is AddProductStep.ScanBarcode -> barcodeAnalyzer.analyze(image)
            is AddProductStep.ScanExpirationDate -> textAnalyzer.analyze(image)
            else -> image.close()
        }
    }
}

