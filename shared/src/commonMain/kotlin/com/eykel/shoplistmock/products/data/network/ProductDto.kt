package com.eykel.shoplistmock.products.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val inStock: Boolean
)

@Serializable
data class ProductListResponseDto(
    val data: List<ProductDto>,
    val total: Int
)

@Serializable
data class ProductDetailDto(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val inStock: Boolean,
    val description: String,
    val rating: Double,
    val stockCount: Int
)
