package com.garde.domain.usecase

import com.garde.domain.model.ProductEntity
import com.garde.domain.repo.ProductRepository
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        barcode: String,
        name: String,
        brand: String?,
        imageUrl: String?,
        expirationDate: String?,
        quantity: Int
    ) {
        val product = ProductEntity(
            barcode = barcode,
            name = name,
            brand = brand,
            imageUrl = imageUrl,
            expirationDate = expirationDate,
            quantity = quantity
        )
        repository.saveProduct(product)
    }
}
