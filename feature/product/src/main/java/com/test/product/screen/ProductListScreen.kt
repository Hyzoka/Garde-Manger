package com.test.product.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                searchText = viewState.searchText,
                onValueChange = {
                    viewModel.onSearchTextChange(it)
                })
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

                else -> ProductGrid(products = viewState.products) { barcode -> }
            }
        }

    }
}

