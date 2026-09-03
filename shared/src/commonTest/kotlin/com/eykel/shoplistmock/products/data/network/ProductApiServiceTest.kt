package com.eykel.shoplistmock.products.data.network

import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.network.NetworkSimulationConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class ProductApiServiceTest {

    private val noLatency = NetworkSimulationConfig(latencyMillis = 0L..0L)

    @Test
    fun getProducts_returnsAllFixtures() = runTest {
        val service = productApiService(noLatency)

        val response = service.getProducts()

        assertEquals(ProductFixtures.all.size, response.total)
        assertEquals(ProductFixtures.summaries().map { it.id }, response.data.map { it.id })
    }

    @Test
    fun getProductDetail_returnsMatchingFixture() = runTest {
        val service = productApiService(noLatency)

        val detail = service.getProductDetail("p1")

        assertEquals("Arroz Integral 1kg", detail.name)
        assertTrue(detail.stockCount > 0)
    }

    @Test
    fun getProductDetail_unknownId_throwsNotFound() = runTest {
        val service = productApiService(noLatency)

        assertFailsWith<NetworkError.NotFound> { service.getProductDetail("does-not-exist") }
    }

    @Test
    fun getProducts_alwaysErrorRate_throwsServerError() = runTest {
        val service = productApiService(noLatency.copy(errorRate = 1f))

        assertFailsWith<NetworkError.Server> { service.getProducts() }
    }
}
