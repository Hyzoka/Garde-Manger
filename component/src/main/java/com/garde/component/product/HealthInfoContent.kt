package com.garde.component.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.garde.domain.model.Product

@Composable
fun HealthInfoContent(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Nutri-Score : ${product.nutriscore ?: "Non disponible"}")
        Text("Nova Group : ${product.novaGroup ?: "Non disponible"}")
        Text("Ingrédients : ${product.ingredients ?: "Non renseignés"}")
        Text("Additifs : ${product.additives ?: "Non renseignés"}")
    }
}
