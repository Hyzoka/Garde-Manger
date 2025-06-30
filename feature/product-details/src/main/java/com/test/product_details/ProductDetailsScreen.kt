package com.test.product_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.garde.core.R
import com.garde.domain.model.Product
import com.garde.domain.model.ProductLot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    barcode: String,
    onBackClick: () -> Unit,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.product_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.error_loading_product),
                            color = Color.Red
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = viewModel::retry) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            else -> {
                ProductDetailsContent(
                    product = uiState.product,
                    lots = uiState.lots,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailsContent(
    product: Product?,
    lots: List<ProductLot>,
    modifier: Modifier = Modifier
) {
    if (product == null) return

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            GlideImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.brand,
                            product.brand ?: stringResource(R.string.not_available)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.barcode_label, product.barcode),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.lots_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (lots.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_lots_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    } else {
                        lots.forEach { lot ->
                            Spacer(Modifier.height(8.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = lot.expirationDate
                                        ?: stringResource(R.string.not_available),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = pluralStringResource(
                                        R.plurals.product_quantity,
                                        lot.quantity,
                                        lot.quantity
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add other sections if needed (Nutrition, Environment, etc.)
    }
}
