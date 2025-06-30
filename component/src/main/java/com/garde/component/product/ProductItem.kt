package com.garde.component.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.garde.component.utils.getLocalizedExpirationMessage
import com.garde.core.R
import com.garde.domain.model.Product
import com.garde.domain.utils.getRemainingDays

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(
    product: Product,
    isEditMode: Boolean,
    onClick: (String) -> Unit,
    onQuantityUpdate: (Product, Int) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
            .height(275.dp)
            .padding(4.dp)
            .clickable { onClick(product.barcode) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlideImage(
                model = product.imageUrl,
                contentDescription = "Image du produit",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
            )

            Text(
                text = product.name.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = getLocalizedExpirationMessage(
                    getRemainingDays(
                        expirationDate = product.expirationDate ?: "Non renseigné"
                    )
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (isEditMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = {
                        val newQty = (product.quantity ?: 1) - 1
                        if (newQty <= 0) onDeleteProduct(product)
                        else onQuantityUpdate(product, newQty)
                    }) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Decrease quantity"
                        )
                    }

                    Text(
                        text = "${product.quantity}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(onClick = {
                        val newQty = (product.quantity ?: 0) + 1
                        onQuantityUpdate(product, newQty)
                    }) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "Increase quantity"
                        )
                    }
                }
            } else {
                Text(
                    text = pluralStringResource(
                        R.plurals.product_quantity,
                        product.quantity ?: 0,
                        product.quantity ?: 0
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

