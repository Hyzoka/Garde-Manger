package com.garde.domain.usecase

import com.garde.domain.ResultState
import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(barcode: String): Flow<ResultState<Product>> {
        return repository.getProductByBarcode(barcode)
    }
}

