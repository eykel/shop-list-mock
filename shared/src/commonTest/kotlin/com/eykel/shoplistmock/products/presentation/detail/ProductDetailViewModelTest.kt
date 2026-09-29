package com.eykel.shoplistmock.products.presentation.detail

import app.cash.turbine.test
import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.fake.FakeProductRepository
import com.eykel.shoplistmock.products.domain.model.ProductDetail
import com.eykel.shoplistmock.products.domain.usecase.GetProductDetailUseCase
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

private val sampleDetail = ProductDetail(
    id = "p1",
    name = "Arroz Integral 1kg",
    price = 8.90,
    imageUrl = "https://example.test/p1.png",
    category = "Graos",
    inStock = true,
    description = "Arroz integral tipo 1.",
    rating = 4.6,
    stockCount = 42
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private val repository = FakeProductRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(productId: String = "p1") =
        ProductDetailViewModel(productId, GetProductDetailUseCase(repository))

    @Test
    fun initialLoad_success_populatesDetail() = runTest {
        repository.detailResult = ModelResult.success(sampleDetail)
        val gate = CompletableDeferred<Unit>()
        repository.detailGate = gate

        viewModel().state.test {
            assertTrue(awaitItem().isLoading)

            gate.complete(Unit)

            val loaded = awaitItem()
            assertTrue(!loaded.isLoading)
            assertEquals(sampleDetail, loaded.detail)
        }
    }

    @Test
    fun initialLoad_failure_setsErrorMessageAndEmitsEffect() = runTest {
        repository.detailResult = ModelResult.error(NetworkError.NotFound("p1"))
        val viewModel = viewModel()

        viewModel.effect.test {
            assertTrue(awaitItem() is ProductDetailEffect.ShowError)
        }
        assertTrue(viewModel.state.value.errorMessage != null)
        assertNull(viewModel.state.value.detail)
    }

    @Test
    fun retry_afterFailure_recoversOnSuccess() = runTest {
        repository.detailResult = ModelResult.error(NetworkError.Timeout)
        val viewModel = viewModel()
        assertTrue(viewModel.state.value.errorMessage != null)

        repository.detailResult = ModelResult.success(sampleDetail)
        viewModel.onAction(ProductDetailAction.Retry)

        assertEquals(sampleDetail, viewModel.state.value.detail)
        assertNull(viewModel.state.value.errorMessage)
    }
}
