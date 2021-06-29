package com.ivan.animals

import com.ivan.animals.di.ApiModule
import com.ivan.animals.model.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {

    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}