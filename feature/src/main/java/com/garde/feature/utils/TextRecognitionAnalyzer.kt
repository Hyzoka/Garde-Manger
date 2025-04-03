package com.garde.feature.utils

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class TextRecognitionAnalyzer(
    private val onTextDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        val inputImage = mediaImage.toBitmap()?.let { InputImage.fromBitmap(it, rotationDegrees) }
        if (inputImage != null) {
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    val detectedText = visionText.text
                    if (detectedText.isNotEmpty()) {
                        Log.i("TextRecognition", "Detected text: $detectedText")
                        onTextDetected(detectedText)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("TextRecognition", "Text recognition failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun Image.toBitmap(): Bitmap? {
        if (format == ImageFormat.YUV_420_888) {
            val yuvBytes = yuv420888ToNv21(this)
            val yuvImage = YuvImage(yuvBytes, ImageFormat.NV21, width, height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
            val imageBytes = out.toByteArray()
            return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }
        return null
    }

    private fun yuv420888ToNv21(image: Image): ByteArray {
        val yBuffer: ByteBuffer = image.planes[0].buffer // Y
        val uBuffer: ByteBuffer = image.planes[1].buffer // U
        val vBuffer: ByteBuffer = image.planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        // Copy Y values
        yBuffer.get(nv21, 0, ySize)

        // Copy VU values
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        return nv21
    }
}
