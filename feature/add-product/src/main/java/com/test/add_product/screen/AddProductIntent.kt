package com.test.add_product.screen

import com.garde.domain.repo.ProductRepository

sealed interface AddProductIntentInterface {
    data class GetProduct(val barcode: String): AddProductIntentInterface
}

class AddProductIntent(private val repository: ProductRepository) {
    suspend fun intent(intent: AddProductIntentInterface, state: AddProductViewState): AddProductViewState {
        val newState = when (intent) {
            is AddProductIntentInterface.GetProduct -> {
                val product = repository.getProductByBarcode(intent.barcode).onFailure {
                    println("ca a failed ptn")
                }.getOrNull()?.copy(quantity = null)
                state.copy(product = product)
            }
        }

        return newState
    }
}
