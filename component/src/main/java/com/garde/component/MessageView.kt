package com.garde.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.garde.core.R

@Composable
fun MessageView(
    imageResId: Int? = null,
    lottie: Int? = null,
    message: Int,
    textColor: Color = Color.Black,
    onClicked: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1F))

        imageResId?.also {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = imageResId.toString(),
                modifier = Modifier.size(150.dp)
            )
        }

        lottie?.also {
            val preloaderLottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(lottie)
            )
            val preloaderProgress by animateLottieCompositionAsState(
                preloaderLottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )

            LottieAnimation(
                composition = preloaderLottieComposition,
                progress = { preloaderProgress },
            )
        }
        Text(
            text = stringResource(message),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1F))

        Button(onClick = onClicked) {
            Text(text = stringResource(R.string.retry))
        }
        Spacer(modifier = Modifier.height(24.dp))

    }
}
