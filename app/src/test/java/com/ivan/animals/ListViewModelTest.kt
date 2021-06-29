package com.ivan.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivan.animals.di.AppModule
import com.ivan.animals.di.DaggerViewModelComponent
import com.ivan.animals.model.Animal
import com.ivan.animals.model.AnimalApiService
import com.ivan.animals.model.ApiKey
import com.ivan.animals.utils.SharedPreferencesHelper
import com.ivan.animals.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class ListViewModelTest {

    @get: Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalService: AnimalApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    val application = Mockito.mock(Application::class.java)
    val listViewModel = ListViewModel(application, true)

    private val key = "Test key"

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent
            .builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(animalService))
            .prefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(
                    Executor { it.run() },
                    true
                )
            }
        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate}
    }

    @Test
    fun getAnimalsSuccess(){
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val animal = Animal("cow", null, null, null, null, null, null)
        val animals = listOf(animal)
        val testSingle: Single<List<Animal>> = Single.just(animals)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getAnimalsFailure() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("ok", key))
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)
        listViewModel.refresh()
        Assert.assertEquals(0, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }

    @Test
    fun getKeySuccess() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
        val apiKey = ApiKey("ok", key)
        val singleTest = Single.just(apiKey)
        Mockito.`when`(animalService.getApiKey()).thenReturn(singleTest)

        val animal = Animal("cow", null, null, null, null, null, null)
        val animals = listOf(animal)
        val testSingle: Single<List<Animal>> = Single.just(animals)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getApiFailure() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
        val testSingle = Single.error<ApiKey>(Throwable())
        Mockito.`when`(animalService.getApiKey()).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }
}