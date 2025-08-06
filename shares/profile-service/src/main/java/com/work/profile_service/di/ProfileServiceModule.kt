package com.work.profile_service.di

import com.work.profile_service.data.service.repository.IProfileRepository
import com.work.profile_service.data.service.repository.ProfileRepository
import com.work.profile_service.data.service.repository.remote.IProfileRemote
import com.work.profile_service.data.service.repository.remote.ProfileRemote
import com.work.profile_service.domain.DateFormatUseCase
import com.work.profile_service.domain.GetProfileUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileServiceModule = module {

    factory<IProfileRemote> {
        ProfileRemote(get())
    }

    factory<IProfileRepository> {
        ProfileRepository(get())
    }

    factory {
        DateFormatUseCase()
    }

    factory {
        GetProfileUseCase(
            get(),
            get(),
            get(named("io"))
        )
    }
}