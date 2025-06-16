package com.garde.domain.usecase

import com.garde.domain.utils.ResultState
import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<ResultState<List<Product>>> {
        return repository.getAllProducts()
    }
}


