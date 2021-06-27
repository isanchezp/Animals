package com.ivan.animals.di

import com.ivan.animals.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, PrefsModule::class, ApiModule::class])
interface ViewModelComponent {

    fun inject(listViewModel: ListViewModel)
}