package com.eykel.shoplistmock.products.presentation.detail

import androidx.lifecycle.viewModelScope
import com.eykel.shoplistmock.core.mvi.MviViewModel
import com.eykel.shoplistmock.core.result.onFailure
import com.eykel.shoplistmock.core.result.onSuccess
import com.eykel.shoplistmock.products.domain.usecase.GetProductDetailUseCase
import kotlinx.coroutines.launch

private const val DEFAULT_LOAD_ERROR = "Nao foi possivel carregar o produto. Verifique sua conexao."

/**
 * One instance per opened product, created with [productId] via Koin `parametersOf` — the
 * detail bottom sheet is never in a state where it does not know which product it is showing.
 */
class ProductDetailViewModel(
    private val productId: String,
    private val getProductDetail: GetProductDetailUseCase
) : MviViewModel<ProductDetailState, ProductDetailAction, ProductDetailEffect>(ProductDetailState()) {

    init {
        load()
    }

    override fun onAction(action: ProductDetailAction) {
        when (action) {
            ProductDetailAction.Load, ProductDetailAction.Retry -> load()
        }
    }

    private fun load() {
        setState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getProductDetail(productId)
                .onSuccess { detail -> setState { copy(isLoading = false, detail = detail) } }
                .onFailure { error ->
                    val message = error.message ?: DEFAULT_LOAD_ERROR
                    setState { copy(isLoading = false, errorMessage = message) }
                    sendEffect(ProductDetailEffect.ShowError(message))
                }
        }
    }
}
