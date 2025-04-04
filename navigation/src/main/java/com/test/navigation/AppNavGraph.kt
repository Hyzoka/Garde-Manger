package com.test.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.test.add_product.nav.addProductsGraph
import com.test.product.nav.productGraph

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String,modifier : Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        productGraph(navController)
        addProductsGraph(navController)
    }
}
