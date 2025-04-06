package com.test.add_product.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.garde.core.DestinationRoute
import com.test.add_product.screen.AddProductScreen

fun NavGraphBuilder.addProductsGraph(navController: NavHostController) {
    composable(DestinationRoute.ADD_PRODUCT_SCREEN, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(500)
        )
    }, exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(500)
        )
    }) {
        AddProductScreen(onPopBackStack = {
            navController.popBackStack()
        })
    }
}
