package com.eykel.shoplistmock.products.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val inStock: Boolean
)

data class ProductDetail(
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
