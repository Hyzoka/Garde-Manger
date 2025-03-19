package com.garde.presentation.screen.home

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
import androidx.navigation.NavController
import com.garde.presentation.component.MessageView
import com.garde.presentation.component.SearchBar
import com.garde.presentation.component.loading.LoadingListSkeleton
import com.garde.presentation.component.product.ProductGrid
import com.garde.presentation.navigation.Screen

@Composable
fun ProductListScreen(
    navController: NavController,
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
                onClick = { navController.navigate(Screen.AddNewProduct.route) },
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
                viewState.error != null -> MessageView(
                    imageResId = com.garde.core.R.drawable.placeholder,
                    message = viewState.error!!,
                    textColor = Color.Red
                )

                viewState.products.isEmpty() -> MessageView(
                    imageResId = com.garde.core.R.drawable.placeholder,
                    message = "Aucun produit trouvé"
                )

                else -> ProductGrid(products = viewState.products) { barcode -> }
            }
        }

    }
}

