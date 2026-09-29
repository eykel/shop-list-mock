package com.eykel.shoplistmock.products.presentation.list

import androidx.lifecycle.viewModelScope
import com.eykel.shoplistmock.core.mvi.MviViewModel
import com.eykel.shoplistmock.core.result.onFailure
import com.eykel.shoplistmock.core.result.onSuccess
import com.eykel.shoplistmock.products.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.launch

private const val DEFAULT_LOAD_ERROR = "Nao foi possivel carregar os produtos. Verifique sua conexao."

class ProductListViewModel(
    private val getProducts: GetProductsUseCase
) : MviViewModel<ProductListState, ProductListAction, ProductListEffect>(ProductListState()) {

    init {
        load()
    }

    override fun onAction(action: ProductListAction) {
        when (action) {
            ProductListAction.Load, ProductListAction.Retry -> load()
            is ProductListAction.SelectProduct -> setState { copy(selectedProductId = action.id) }
            ProductListAction.DismissDetail -> setState { copy(selectedProductId = null) }
        }
    }

    private fun load() {
        setState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getProducts()
                .onSuccess { products -> setState { copy(isLoading = false, products = products) } }
                .onFailure { error ->
                    val message = error.message ?: DEFAULT_LOAD_ERROR
                    setState { copy(isLoading = false, errorMessage = message) }
                    sendEffect(ProductListEffect.ShowError(message))
                }
        }
    }
}
