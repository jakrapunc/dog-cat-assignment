package com.work.cat.di

import com.work.cat.screen.CatScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val catModule = module {
    viewModel {
        CatScreenViewModel(
            get(),
            get(named("io")),
        )
    }
}