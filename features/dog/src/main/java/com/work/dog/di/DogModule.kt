package com.work.dog.di

import com.work.dog.screen.DogScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dogModule = module {

    viewModel {
        DogScreenViewModel(
            get(),
            get(),
            get(named("io"))
        )
    }
}