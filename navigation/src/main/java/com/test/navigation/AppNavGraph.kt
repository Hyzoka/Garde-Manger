package com.test.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.test.add_product.screen.AddProductScreen
import com.test.product.screen.ProductListScreen
import com.test.product_details.ProductDetailsScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        // List screen
        composable(DestinationRoute.PRODUCT_SCREEN) {
            ProductListScreen(
                onAddProductClick = {
                    navController.navigate(DestinationRoute.ADD_PRODUCT_SCREEN)
                },
                onProductClick = { barcode ->
                    navController.navigate("${DestinationRoute.PRODUCT_DETAIL_SCREEN}/$barcode")
                }
            )
        }

        // Add product screen
        composable(
            DestinationRoute.ADD_PRODUCT_SCREEN,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            AddProductScreen(
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }

        // Detail screen with argument
        composable(
            route = DestinationRoute.PRODUCT_DETAIL_SCREEN_WITH_ARG,
            arguments = listOf(navArgument(DestinationRoute.PassedKey.BARCODE_VALUE) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val barcode =
                backStackEntry.arguments?.getString(DestinationRoute.PassedKey.BARCODE_VALUE) ?: ""
            ProductDetailsScreen(
                barcode = barcode,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }

}
