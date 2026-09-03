package com.eykel.shoplistmock.products.data.repository

import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.products.data.network.ProductApiService
import com.eykel.shoplistmock.products.data.network.ProductDetailDto
import com.eykel.shoplistmock.products.data.network.ProductDto
import com.eykel.shoplistmock.products.data.network.ProductListResponseDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.test.runTest

private class FakeProductApiService(
    private val products: List<ProductDto> = emptyList(),
    private val detail: ProductDetailDto? = null,
    private val failure: Throwable? = null
) : ProductApiService {
    override suspend fun getProducts(): ProductListResponseDto {
        failure?.let { throw it }
        return ProductListResponseDto(products, products.size)
    }

    override suspend fun getProductDetail(id: String): ProductDetailDto {
        failure?.let { throw it }
        return requireNotNull(detail)
    }
}

private val sampleDto = ProductDto(
    id = "p1",
    name = "Arroz Integral 1kg",
    price = 8.90,
    imageUrl = "https://example.test/p1.png",
    category = "Graos",
    inStock = true
)

private val sampleDetailDto = ProductDetailDto(
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

class ProductRepositoryImplTest {

    @Test
    fun getProducts_success_mapsDtoToDomain() = runTest {
        val repository = ProductRepositoryImpl(FakeProductApiService(products = listOf(sampleDto)))

        val result = repository.getProducts()

        assertIs<ModelResult<List<*>>>(result)
        assertEquals(ModelResult.Status.SUCCESS, result.status)
        assertEquals("Arroz Integral 1kg", result.data?.first()?.name)
    }

    @Test
    fun getProductDetail_success_mapsDtoToDomain() = runTest {
        val repository = ProductRepositoryImpl(FakeProductApiService(detail = sampleDetailDto))

        val result = repository.getProductDetail("p1")

        assertEquals(ModelResult.Status.SUCCESS, result.status)
        assertEquals(42, result.data?.stockCount)
    }

    @Test
    fun getProducts_networkError_isPassedThroughUntouched() = runTest {
        val repository = ProductRepositoryImpl(FakeProductApiService(failure = NetworkError.Server(500)))

        val result = repository.getProducts()

        assertEquals(ModelResult.Status.ERROR, result.status)
        assertIs<NetworkError.Server>(result.throwable)
    }

    @Test
    fun getProducts_unexpectedThrowable_isWrappedAsUnknown() = runTest {
        val repository = ProductRepositoryImpl(FakeProductApiService(failure = IllegalStateException("boom")))

        val result = repository.getProducts()

        assertEquals(ModelResult.Status.ERROR, result.status)
        assertIs<NetworkError.Unknown>(result.throwable)
    }
}
