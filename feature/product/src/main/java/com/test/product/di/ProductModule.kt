package com.test.product.di

val productModule = module {
    viewModel { ProductListViewModel(get(), get()) }
    // Ajoute d'autres ViewModels ou UseCases si besoin
}