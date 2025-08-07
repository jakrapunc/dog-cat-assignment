package com.work.dog_service.di

import com.work.dog_service.data.service.repository.DogRepository
import com.work.dog_service.data.service.repository.IDogRepository
import com.work.dog_service.data.service.repository.remote.DogRemote
import com.work.dog_service.data.service.repository.remote.IDogRemote
import com.work.dog_service.domain.ConvertTimeStampUseCase
import com.work.dog_service.domain.GetConcurrentDogListUseCase
import com.work.dog_service.domain.GetSequenceDogListUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dogServiceModule = module {
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

    factory<GetSequenceDogListUseCase> {
        GetSequenceDogListUseCase(
            get(),
            get(),
            get(named("io"))
        )
    }

    factory<GetConcurrentDogListUseCase> {
        GetConcurrentDogListUseCase(
            get(),
            get(),
            get(named("io"))
        )
    }

    factory<ConvertTimeStampUseCase> {
        ConvertTimeStampUseCase()
    }
}