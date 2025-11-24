package com.test.add_product.di

import com.garde.feature.addproduct.AddProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addProductModule = module {
    viewModel { AddProductViewModel(get(), get()) }
    // Ajoute d'autres ViewModels ou UseCases si besoin
}