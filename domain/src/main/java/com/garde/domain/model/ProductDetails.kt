package com.garde.domain.model

data class ProductDetails(
    val product: Product,                   // infos de l'API
    val lots: List<ProductLot>              // données locales Room
)
