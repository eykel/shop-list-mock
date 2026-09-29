package com.eykel.shoplistmock.products.domain.repository

import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.domain.model.ProductDetail

interface ProductRepository {
    suspend fun getProducts(): ModelResult<List<Product>>
    suspend fun getProductDetail(id: String): ModelResult<ProductDetail>
}
