package com.test.product.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.garde.component.SearchBar
import com.garde.component.loading.LoadingListSkeleton
import com.garde.component.product.ProductGrid

@Composable
fun ProductListScreen(
    onAddProductClick: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchBar(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    searchText = viewState.searchText,
                    onValueChange = {
                        viewModel.onSearchTextChange(it)
                    })

                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            viewModel.isEditing(viewState.editStockedProduct.isEditing.not())
                        },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer un produit"
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter un produit")
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            when {
                viewState.isLoading -> LoadingListSkeleton()
                viewState.error != null -> {
                    //TODO afficher un message d'erreur
                }

                viewState.products.isEmpty() -> {
                    //TODO afficher un message d'erreur
                    //MessageView(
                    //    imageResId = com.garde.core.R.drawable.placeholder,
                    //    message = "Aucun produit trouvé"
                    //)
                }

                else -> ProductGrid(
                    products = viewState.products,
                    isEditing = viewState.editStockedProduct.isEditing,
                    onProductClick = { onProductClick(it) },
                    onQuantityUpdate = { prod, newQty -> viewModel.updateQuantity(prod, newQty) },
                    onDeleteProduct = { viewModel.deleteProduct(it) })
            }
        }

    }
}

