package com.ivan.animals.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ivan.animals.di.AppModule
import com.ivan.animals.di.CONTEXT_APP
import com.ivan.animals.di.DaggerViewModelComponent
import com.ivan.animals.di.TypeOfContext
import com.ivan.animals.model.Animal
import com.ivan.animals.model.AnimalApiService
import com.ivan.animals.model.AnimalsResponse
import com.ivan.animals.model.ApiKey
import com.ivan.animals.utils.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(app: Application): AndroidViewModel(app) {

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharedPreferencesHelper

    private var invalidateApiKey = false

    init {
        DaggerViewModelComponent
            .builder()
            .appModule(AppModule(getApplication()))
            .build()
            .inject(this)
    }

    fun refresh() {
        loading.value = true
        invalidateApiKey = false
        val key = prefs.getApiKey()
        if (key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }
        Log.d("refresh()", "Refreshing data")
    }

    private fun getKey() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) {
                        if (key.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                        Log.d("getKey()", key.toString())
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                        Log.d("getKey()", "An error happened")
                    }
                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AnimalsResponse>() {
                    override fun onSuccess(response: AnimalsResponse) {
                        loadError.value = false
                        loading.value = false
                        animals.value = response
                        Log.d("getAnimals()", response.toString())
                    }

                    override fun onError(e: Throwable) {
                        if (!invalidateApiKey) {
                            invalidateApiKey = true
                            getKey()
                        } else {
                            e.printStackTrace()
                            loadError.value = true
                            loading.value = false
                            animals.value = arrayListOf()
                            Log.d("getAnimals()", "An error happened")
                        }
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}