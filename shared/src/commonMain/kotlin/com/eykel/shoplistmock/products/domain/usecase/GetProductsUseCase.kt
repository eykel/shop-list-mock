package com.eykel.shoplistmock.products.domain.usecase

import com.eykel.shoplistmock.core.result.ModelResult
import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): ModelResult<List<Product>> = repository.getProducts()
}
