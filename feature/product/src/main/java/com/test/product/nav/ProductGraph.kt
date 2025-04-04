package com.test.product.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.garde.core.DestinationRoute
import com.test.product.screen.ProductListScreen

fun NavGraphBuilder.productGraph(navController: NavHostController) {
    composable(DestinationRoute.PRODUCT_SCREEN) {
        ProductListScreen(navController)
    }
}
