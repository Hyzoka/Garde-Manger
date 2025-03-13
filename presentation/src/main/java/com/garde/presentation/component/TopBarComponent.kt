package com.garde.manger.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TopBarComponent(
    titleResId: Int, // ID du texte depuis strings.xml
    icon: @Composable () -> Unit, // Icône personnalisable
    onIconClick: () -> Unit // Action au clic de l’icône
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
        Text(
            text = stringResource(id = titleResId),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}
