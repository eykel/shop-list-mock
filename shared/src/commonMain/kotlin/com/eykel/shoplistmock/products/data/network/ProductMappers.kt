package com.eykel.shoplistmock.products.data.network

import com.eykel.shoplistmock.products.domain.model.Product
import com.eykel.shoplistmock.products.domain.model.ProductDetail

internal fun ProductDto.toDomain(): Product = Product(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl,
    category = category,
    inStock = inStock
)

internal fun ProductDetailDto.toDomain(): ProductDetail = ProductDetail(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl,
    category = category,
    inStock = inStock,
    description = description,
    rating = rating,
    stockCount = stockCount
)
