package com.eykel.shoplistmock

import android.app.Application
import com.eykel.shoplistmock.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ShopListMockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@ShopListMockApplication)
        }
    }
}
