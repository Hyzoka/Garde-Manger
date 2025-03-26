package com.garde.domain.usecase

import com.garde.domain.ResultState
import com.garde.domain.model.Product
import com.garde.domain.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
     operator fun invoke(product: Product): Flow<ResultState<Unit>> {
        return repository.saveProduct(product)
    }
}

