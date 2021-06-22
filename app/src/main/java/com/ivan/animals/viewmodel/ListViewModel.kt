package com.ivan.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ivan.animals.model.Animal

class ListViewModel(app: Application): AndroidViewModel(app) {

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    fun refresh() {
        getAnimals()
    }

    private fun getAnimals(){
        animals.value = mutableListOf(
            Animal("alligator"),
            Animal("bee"),
            Animal("cat"),
            Animal("dog"),
            Animal("elephant"),
            Animal("flamingo"))

        loadError.value = false
        loading.value = false
    }
}