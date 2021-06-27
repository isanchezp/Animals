package com.ivan.animals.di

import com.ivan.animals.model.AnimalApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(animalApiService: AnimalApiService)
}