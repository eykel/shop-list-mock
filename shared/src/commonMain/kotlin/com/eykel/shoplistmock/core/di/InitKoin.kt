package com.eykel.shoplistmock.core.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * One entry point for both platforms. `androidApp` calls this with `androidContext(this)`
 * appended through [config]; iOS calls it with no extra declaration.
 */
fun initKoin(config: KoinAppDeclaration? = null): KoinApplication = startKoin {
    config?.invoke(this)
    modules(coreModule)
}
