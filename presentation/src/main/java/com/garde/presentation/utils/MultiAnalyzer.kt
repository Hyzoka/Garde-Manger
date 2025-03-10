package com.garde.presentation.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.garde.presentation.screen.addproduct.AddProductStep
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

class MultiAnalyzer(
    private val barcodeAnalyzer: BarcodeScanningAnalyzer,
    private val textAnalyzer: TextRecognitionAnalyzer,
    private val stepProvider: () -> AddProductStep
) : ImageAnalysis.Analyzer {

    private val activeAnalyzer = AtomicReference<ImageAnalysis.Analyzer>(barcodeAnalyzer)

    override fun analyze(image: ImageProxy) {
        Timber.i("ZAEAZE step = $stepProvider")
        when (stepProvider.invoke()) {
            is AddProductStep.ScanBarcode -> activeAnalyzer.set(barcodeAnalyzer)
            is AddProductStep.ScanExpirationDate -> activeAnalyzer.set(textAnalyzer)
            else -> return
        }
        activeAnalyzer.get().analyze(image)
    }
}

