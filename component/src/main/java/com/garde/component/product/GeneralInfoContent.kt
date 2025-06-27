package com.garde.component.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.garde.domain.model.Product

@Composable
fun GeneralInfoContent(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Nom : ${product.name}")
        Text("Marque : ${product.brand ?: "Non renseigné"}")
        Text("Code-barres : ${product.barcode}")
        Text("Date de péremption : ${product.expirationDate ?: "Non renseignée"}")
    }
}
