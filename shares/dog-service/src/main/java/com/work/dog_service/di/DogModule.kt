package com.work.dog_service.di

import com.work.dog_service.data.service.repository.DogRepository
import com.work.dog_service.data.service.repository.IDogRepository
import com.work.dog_service.data.service.repository.remote.DogRemote
import com.work.dog_service.data.service.repository.remote.IDogRemote
import org.koin.dsl.module

val dogModule = module {
    factory<IDogRemote> {
        DogRemote(
            get(),
        )
    }

    factory<IDogRepository> {
        DogRepository(
            get()
        )
    }
}