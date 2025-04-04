package com.test.add_product.component

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeRect
import com.google.mlkit.vision.barcode.common.Barcode
import com.test.add_product.utils.drawBounds

@Composable
fun DrawBarcode(
    barcodes: List<Barcode>,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        barcodes.forEach { barcode ->
            barcode.boundingBox?.toComposeRect()?.let {
                val topLeft = com.test.add_product.utils.adjustPoint(
                    PointF(it.topLeft.x, it.topLeft.y),
                    imageWidth,
                    imageHeight,
                    screenWidth,
                    screenHeight
                )
                val size = com.test.add_product.utils.adjustSize(
                    it.size,
                    imageWidth,
                    imageHeight,
                    screenWidth,
                    screenHeight
                )
                drawBounds(topLeft, size, Color.White, 8f)
            }
        }
    }
}