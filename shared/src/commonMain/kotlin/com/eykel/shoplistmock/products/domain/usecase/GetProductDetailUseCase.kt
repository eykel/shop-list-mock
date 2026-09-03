package com.eykel.shoplistmock.products.domain.usecase

import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.products.domain.model.ProductDetail
import com.eykel.shoplistmock.products.domain.repository.ProductRepository

class GetProductDetailUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: String): ModelResult<ProductDetail> = repository.getProductDetail(id)
}
