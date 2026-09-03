package com.eykel.shoplistmock.core.di

import com.eykel.shoplistmock.products.domain.usecase.GetProductDetailUseCase
import com.eykel.shoplistmock.products.domain.usecase.GetProductsUseCase
import kotlin.test.AfterTest
import kotlin.test.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class InitKoinTest : KoinTest {

    private val getProducts: GetProductsUseCase by inject()
    private val getProductDetail: GetProductDetailUseCase by inject()

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun initKoin_resolvesTheFullProductsGraph() {
        initKoin()

        getProducts
        getProductDetail
    }
}
