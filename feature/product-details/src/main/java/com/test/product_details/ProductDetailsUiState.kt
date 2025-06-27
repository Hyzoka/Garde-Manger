package com.test.product_details

import com.garde.domain.model.Product
import com.garde.domain.model.ProductLot

data class ProductDetailsUiState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val lots: List<ProductLot> = emptyList(),
    val error: String? = null
)
