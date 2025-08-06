package com.work.cnd

import android.app.Application
import com.work.cat.di.catModule
import com.work.cat_service.di.catServiceModule
import com.work.dog.di.dogModule
import com.work.dog_service.di.dogServiceModule
import com.work.network.di.networkModule
import com.work.profile_service.di.profileServiceModule
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
    catServiceModule,
    dogServiceModule,
    profileServiceModule,
    catModule,
    dogModule,
)