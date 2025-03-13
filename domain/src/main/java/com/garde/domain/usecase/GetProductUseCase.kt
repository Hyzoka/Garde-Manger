package com.garde.domain.usecase

import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Product? {
        return repository.getProductApi(barcode)
    }
}
