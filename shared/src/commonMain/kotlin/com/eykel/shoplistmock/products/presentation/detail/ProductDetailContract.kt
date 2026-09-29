package com.eykel.shoplistmock.products.presentation.detail

import com.eykel.shoplistmock.core.mvi.UiAction
import com.eykel.shoplistmock.core.mvi.UiEffect
import com.eykel.shoplistmock.core.mvi.UiState
import com.eykel.shoplistmock.products.domain.model.ProductDetail

data class ProductDetailState(
    val isLoading: Boolean = true,
    val detail: ProductDetail? = null,
    val errorMessage: String? = null
) : UiState

sealed interface ProductDetailAction : UiAction {
    data object Load : ProductDetailAction
    data object Retry : ProductDetailAction
}

sealed interface ProductDetailEffect : UiEffect {
    data class ShowError(val message: String) : ProductDetailEffect
}
