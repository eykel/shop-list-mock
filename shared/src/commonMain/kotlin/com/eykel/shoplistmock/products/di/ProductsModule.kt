package com.eykel.shoplistmock.products.di

import com.eykel.shoplistmock.core.network.NetworkSimulationConfig
import com.eykel.shoplistmock.products.data.network.ProductApiService
import com.eykel.shoplistmock.products.data.network.productApiService
import com.eykel.shoplistmock.products.data.repository.ProductRepositoryImpl
import com.eykel.shoplistmock.products.domain.repository.ProductRepository
import com.eykel.shoplistmock.products.domain.usecase.GetProductDetailUseCase
import com.eykel.shoplistmock.products.domain.usecase.GetProductsUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The only place [NetworkSimulationConfig] is instantiated — tune latency/error rate here to
 * demo the UI's loading/error states without touching any other layer.
 */
val productsModule: Module = module {
    single { NetworkSimulationConfig() }
    single<ProductApiService> { productApiService(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    factoryOf(::GetProductsUseCase)
    factoryOf(::GetProductDetailUseCase)
}
