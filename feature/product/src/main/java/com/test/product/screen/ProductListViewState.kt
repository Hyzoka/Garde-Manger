package com.test.product.screen

import com.garde.domain.model.Product

data class ProductListViewState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val searchText: String = "",
    val editStockedProduct: EditStockedProductViewState = EditStockedProductViewState()
)

data class EditStockedProductViewState(val isEditing: Boolean = false)
