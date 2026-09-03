package com.eykel.shoplistmock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eykel.shoplistmock.designsystem.ShopListMockTheme

/**
 * Shared root composable. The product list screen replaces this placeholder as the
 * network/repository/presentation layers land — see PROGRESS in the README.
 */
@Composable
fun App() {
    ShopListMockTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Shop List Mock", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}
