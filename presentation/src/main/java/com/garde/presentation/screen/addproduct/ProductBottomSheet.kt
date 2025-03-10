package com.garde.presentation.screen.addproduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.garde.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(product: Product, viewModel: AddProductViewModel) {
    ModalBottomSheet(
        onDismissRequest = { /* Nothing */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.name ?: "Nom inconnu", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Marque: ${product.brand ?: "Non renseigné"}",
                style = MaterialTheme.typography.bodyLarge
            )
            AsyncImage(model = product.imageUrl, contentDescription = "Image du produit")

            Button(onClick = { }) {
                Text("Scanner la date de péremption")
            }
        }
    }
}
