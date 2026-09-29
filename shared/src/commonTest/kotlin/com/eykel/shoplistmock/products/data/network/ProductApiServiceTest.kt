package com.eykel.shoplistmock.products.data.network

import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.network.NetworkSimulationConfig
import com.eykel.shoplistmock.core.network.NetworkSimulationController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

private val noLatency = NetworkSimulationConfig(latencyMillis = 0L..0L)

private fun service(config: NetworkSimulationConfig = noLatency) =
    productApiService(NetworkSimulationController(config))

class ProductApiServiceTest {

    @Test
    fun getProducts_returnsAllFixtures() = runTest {
        val response = service().getProducts()

        assertEquals(ProductFixtures.all.size, response.total)
        assertEquals(ProductFixtures.summaries().map { it.id }, response.data.map { it.id })
    }

    @Test
    fun getProductDetail_returnsMatchingFixture() = runTest {
        val detail = service().getProductDetail("p1")

        assertEquals("Arroz Integral 1kg", detail.name)
        assertTrue(detail.stockCount > 0)
    }

    @Test
    fun getProductDetail_unknownId_throwsNotFound() = runTest {
        assertFailsWith<NetworkError.NotFound> { service().getProductDetail("does-not-exist") }
    }

    @Test
    fun getProducts_alwaysErrorRate_throwsServerError() = runTest {
        assertFailsWith<NetworkError.Server> {
            service(noLatency.copy(errorRate = 1f)).getProducts()
        }
    }
}
