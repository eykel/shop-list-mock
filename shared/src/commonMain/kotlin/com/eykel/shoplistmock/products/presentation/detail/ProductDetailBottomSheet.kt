package com.eykel.shoplistmock.products.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.eykel.shoplistmock.core.format.toBRLCurrency
import com.eykel.shoplistmock.core.ui.ObserveAsEvents
import com.eykel.shoplistmock.designsystem.AppSpacing
import com.eykel.shoplistmock.designsystem.components.ErrorState
import com.eykel.shoplistmock.designsystem.components.LoadingState
import com.eykel.shoplistmock.products.domain.model.ProductDetail
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailBottomSheetRoot(
    productId: String,
    onDismiss: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel(parameters = { parametersOf(productId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is ProductDetailEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        ProductDetailContent(
            state = state,
            onRetry = { viewModel.onAction(ProductDetailAction.Retry) }
        )
        SnackbarHost(snackbarHostState)
    }
}

@Composable
private fun ProductDetailContent(state: ProductDetailState, onRetry: () -> Unit) {
    val detail = state.detail
    when {
        detail != null -> ProductDetailBody(detail)
        state.errorMessage != null -> ErrorState(
            message = state.errorMessage,
            onRetry = onRetry,
            modifier = Modifier.fillMaxWidth().height(240.dp)
        )
        else -> LoadingState(modifier = Modifier.fillMaxWidth().height(240.dp))
    }
}

@Composable
private fun ProductDetailBody(detail: ProductDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.m.dp)
            .padding(bottom = AppSpacing.xl.dp)
    ) {
        AsyncImage(
            model = detail.imageUrl,
            contentDescription = detail.name,
            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(AppSpacing.m.dp))
        Text(
            text = detail.category,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = detail.name, style = MaterialTheme.typography.headlineSmall)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = AppSpacing.xs.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = " ${detail.rating}",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = detail.price.toBRLCurrency(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = AppSpacing.s.dp)
        )
        Text(
            text = if (detail.inStock) "${detail.stockCount} em estoque" else "Produto esgotado",
            style = MaterialTheme.typography.labelMedium,
            color = if (detail.inStock) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
        )
        Text(
            text = detail.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = AppSpacing.m.dp)
        )
        Button(
            onClick = {},
            enabled = detail.inStock,
            modifier = Modifier.fillMaxWidth().padding(top = AppSpacing.l.dp)
        ) {
            Text(if (detail.inStock) "Adicionar a lista" else "Indisponivel")
        }
    }
}
