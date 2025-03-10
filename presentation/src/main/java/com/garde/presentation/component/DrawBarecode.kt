package com.garde.presentation.component

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeRect
import com.garde.presentation.utils.adjustPoint
import com.garde.presentation.utils.adjustSize
import com.garde.presentation.utils.drawBounds
import com.google.mlkit.vision.barcode.common.Barcode

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
                val topLeft = adjustPoint(
                    PointF(it.topLeft.x, it.topLeft.y),
                    imageWidth,
                    imageHeight,
                    screenWidth,
                    screenHeight
                )
                val size = adjustSize(it.size, imageWidth, imageHeight, screenWidth, screenHeight)
                drawBounds(topLeft, size, Color.White, 8f)
            }
        }
    }
}