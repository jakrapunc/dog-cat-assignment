package com.work.cnd

import android.app.Application
import com.work.cat.di.catModule
import com.work.cat_service.di.catServiceModule
import com.work.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CatAndDogApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(androidContext = this@CatAndDogApplication)
            modules(appModule)
        }
    }
}

val appModule = listOf(
    networkModule,
    catModule,
    catServiceModule
)