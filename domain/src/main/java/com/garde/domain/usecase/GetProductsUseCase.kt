package com.garde.domain.usecase

import com.garde.domain.model.ProductEntity
import com.garde.domain.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<ProductEntity>> {
        return repository.getAllProducts()
    }
}
