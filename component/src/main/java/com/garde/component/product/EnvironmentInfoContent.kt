package com.garde.component.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.garde.domain.model.Product

@Composable
fun EnvironmentInfoContent(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Éco-Score : ${product.ecoscore ?: "Non disponible"}")
        Text("Labels : ${product.labels ?: "Aucun"}")
    }
}
