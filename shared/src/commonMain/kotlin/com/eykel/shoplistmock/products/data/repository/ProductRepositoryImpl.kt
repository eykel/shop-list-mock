package com.eykel.shoplistmock.products.data.repository

import com.eykel.shoplistmock.core.network.NetworkError
import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.products.data.network.ProductApiService
import com.eykel.shoplistmock.products.data.network.toDomain
import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.domain.model.ProductDetail
import com.eykel.shoplistmock.products.domain.repository.ProductRepository

/**
 * Never lets a raw exception cross into the presentation layer: [NetworkError] travels as-is
 * (it is already typed), anything else is wrapped into [NetworkError.Unknown] so a ViewModel
 * only ever pattern-matches on one error type.
 */
internal class ProductRepositoryImpl(private val api: ProductApiService) : ProductRepository {

    override suspend fun getProducts(): ModelResult<List<Product>> = safeCall {
        api.getProducts().data.map { it.toDomain() }
    }

    override suspend fun getProductDetail(id: String): ModelResult<ProductDetail> = safeCall {
        api.getProductDetail(id).toDomain()
    }

    private suspend fun <T> safeCall(block: suspend () -> T): ModelResult<T> = try {
        ModelResult.success(block())
    } catch (error: NetworkError) {
        ModelResult.error(error)
    } catch (t: Throwable) {
        ModelResult.error(NetworkError.Unknown(t))
    }
}
