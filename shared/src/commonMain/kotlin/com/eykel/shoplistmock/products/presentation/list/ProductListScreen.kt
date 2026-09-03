package com.eykel.shoplistmock.products.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eykel.shoplistmock.core.ui.ObserveAsEvents
import com.eykel.shoplistmock.designsystem.AppSpacing
import com.eykel.shoplistmock.designsystem.components.EmptyState
import com.eykel.shoplistmock.designsystem.components.ErrorState
import com.eykel.shoplistmock.designsystem.components.LoadingState
import com.eykel.shoplistmock.designsystem.components.ProductCard
import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.presentation.detail.ProductDetailBottomSheetRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductListScreenRoot(viewModel: ProductListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is ProductListEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shop List Mock") },
                actions = {
                    IconButton(
                        onClick = { viewModel.onAction(ProductListAction.ToggleSimulatedFailure) }
                    ) {
                        Icon(
                            imageVector = if (state.simulateFailures) Icons.Filled.WifiOff else Icons.Filled.Wifi,
                            contentDescription = if (state.simulateFailures) {
                                "Falha de rede simulada ativa, tocar para desativar"
                            } else {
                                "Simular falha de rede"
                            },
                            tint = if (state.simulateFailures) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        ProductListScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(padding)
        )
    }

    state.selectedProductId?.let { productId ->
        ProductDetailBottomSheetRoot(
            productId = productId,
            onDismiss = { viewModel.onAction(ProductListAction.DismissDetail) }
        )
    }
}

@Composable
private fun ProductListScreen(
    state: ProductListState,
    onAction: (ProductListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.products.isEmpty() -> LoadingState(modifier)

        state.errorMessage != null && state.products.isEmpty() -> ErrorState(
            message = state.errorMessage,
            onRetry = { onAction(ProductListAction.Retry) },
            modifier = modifier
        )

        state.products.isEmpty() -> EmptyState("Nenhum produto encontrado.", modifier)

        else -> ProductGrid(
            products = state.products,
            onProductClick = { id -> onAction(ProductListAction.SelectProduct(id)) },
            modifier = modifier
        )
    }
}

@Composable
private fun ProductGrid(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(AppSpacing.m.dp),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.s.dp),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.s.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(product = product, onClick = { onProductClick(product.id) })
        }
    }
}
