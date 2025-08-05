package com.work.cat_service.data.di

import com.work.cat_service.data.service.repository.CatRepository
import com.work.cat_service.data.service.repository.ICatRepository
import com.work.cat_service.data.service.repository.remote.CatRemote
import com.work.cat_service.data.service.repository.remote.ICatRemote
import org.koin.core.qualifier.named
import org.koin.dsl.module

val catServiceModule = module {
    factory<ICatRemote> {
        CatRemote(
            get(),
            get(named("io"))
        )
    }

    factory<ICatRepository> {
        CatRepository(
            get()
        )
    }
}