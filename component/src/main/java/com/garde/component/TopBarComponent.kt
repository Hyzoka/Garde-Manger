package com.garde.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TopBarComponent(
    titleResId: Int? = null,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "back",
            tint = Color.Black
        )
    },
    onIconClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = onIconClick) {
            icon()
        }
        Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l’icône et le texte
        titleResId?.also {
            Text(
                text = stringResource(id = titleResId),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        }
    }
}
