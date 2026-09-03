package com.eykel.shoplistmock.products.presentation.list

import com.eykel.shoplistmock.core.mvi.UiAction
import com.eykel.shoplistmock.core.mvi.UiEffect
import com.eykel.shoplistmock.core.mvi.UiState
import com.eykel.shoplistmock.products.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
    // Drives the detail bottom sheet: non-null means it is open for this product id.
    val selectedProductId: String? = null
) : UiState

sealed interface ProductListAction : UiAction {
    data object Load : ProductListAction
    data object Retry : ProductListAction
    data class SelectProduct(val id: String) : ProductListAction
    data object DismissDetail : ProductListAction
}

sealed interface ProductListEffect : UiEffect {
    data class ShowError(val message: String) : ProductListEffect
}
