package com.stanroy.doggofacts.di

import com.stanroy.doggofacts.viewmodel.FactListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {

    val provider: Module = module {
        viewModel { FactListViewModel(get(), get()) }
    }

}