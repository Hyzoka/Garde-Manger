package com.garde.component.product

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.garde.domain.model.Product

@Composable
fun QuantityInfoContent(product: Product) {
    Text("Quantité en stock : ${product.quantity ?: 0}")
}
