package com.eykel.shoplistmock.fake

import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.domain.model.ProductDetail
import com.eykel.shoplistmock.products.domain.repository.ProductRepository
import kotlinx.coroutines.CompletableDeferred

class FakeProductRepository(
    var productsResult: ModelResult<List<Product>> = ModelResult.success(emptyList()),
    var detailResult: ModelResult<ProductDetail> = ModelResult.error(IllegalStateException("not configured"))
) : ProductRepository {

    /** When set, [getProducts] suspends until the test completes it — lets a test observe the loading state. */
    var productsGate: CompletableDeferred<Unit>? = null

    override suspend fun getProducts(): ModelResult<List<Product>> {
        productsGate?.await()
        return productsResult
    }

    override suspend fun getProductDetail(id: String): ModelResult<ProductDetail> = detailResult
}
