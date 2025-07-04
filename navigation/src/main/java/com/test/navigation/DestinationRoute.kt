package com.test.navigation

object DestinationRoute {
    const val PRODUCT_SCREEN = "product_screen_route"
    const val ADD_PRODUCT_SCREEN = "add_product_screen_route"
    const val PRODUCT_DETAIL_SCREEN = "product_detail_screen_route"
    const val PRODUCT_DETAIL_SCREEN_WITH_ARG = "product_detail_screen_route/{barcode}"

    object PassedKey {
        const val BARCODE_VALUE = "barcode"
    }
}
