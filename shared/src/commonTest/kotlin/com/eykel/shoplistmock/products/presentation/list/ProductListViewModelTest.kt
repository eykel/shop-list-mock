package com.eykel.shoplistmock.products.presentation.list

import app.cash.turbine.test
import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.fake.FakeProductRepository
import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.domain.usecase.GetProductsUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

private val sampleProduct = Product(
    id = "p1",
    name = "Arroz Integral 1kg",
    price = 8.90,
    imageUrl = "https://example.test/p1.png",
    category = "Graos",
    inStock = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private val repository = FakeProductRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = ProductListViewModel(GetProductsUseCase(repository))

    @Test
    fun initialLoad_success_populatesProducts() = runTest {
        repository.productsResult = ModelResult.success(listOf(sampleProduct))
        val gate = CompletableDeferred<Unit>()
        repository.productsGate = gate

        viewModel().state.test {
            assertTrue(awaitItem().isLoading)

            gate.complete(Unit)

            val loaded = awaitItem()
            assertTrue(!loaded.isLoading)
            assertEquals(listOf(sampleProduct), loaded.products)
            assertNull(loaded.errorMessage)
        }
    }

    @Test
    fun initialLoad_failure_setsErrorMessageAndEmitsEffect() = runTest {
        repository.productsResult = ModelResult.error(NetworkError.Server(500))
        val viewModel = viewModel()

        viewModel.effect.test {
            assertTrue(awaitItem() is ProductListEffect.ShowError)
        }
        assertTrue(viewModel.state.value.errorMessage != null)
        assertTrue(!viewModel.state.value.isLoading)
    }

    @Test
    fun retry_afterFailure_recoversOnSuccess() = runTest {
        repository.productsResult = ModelResult.error(NetworkError.Timeout)
        val viewModel = viewModel()
        assertTrue(viewModel.state.value.errorMessage != null)

        repository.productsResult = ModelResult.success(listOf(sampleProduct))
        viewModel.onAction(ProductListAction.Retry)

        assertEquals(listOf(sampleProduct), viewModel.state.value.products)
        assertNull(viewModel.state.value.errorMessage)
    }

    @Test
    fun selectProduct_thenDismiss_togglesSelectedId() = runTest {
        val viewModel = viewModel()

        viewModel.onAction(ProductListAction.SelectProduct("p1"))
        assertEquals("p1", viewModel.state.value.selectedProductId)

        viewModel.onAction(ProductListAction.DismissDetail)
        assertNull(viewModel.state.value.selectedProductId)
    }
}
