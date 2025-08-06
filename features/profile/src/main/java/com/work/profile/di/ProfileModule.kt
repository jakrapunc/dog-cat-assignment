package com.work.profile.di

import com.work.profile.screen.ProfileScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {
    viewModel {
        ProfileScreenViewModel(
            get(),
            get(named("io"))
        )
    }
}