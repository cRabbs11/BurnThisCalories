package com.ekochkov.burnthiscalories.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.PrePopulateDB
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.domain.Interactor
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainFragmentViewModel: ViewModel() {

    val burnListLiveData = MutableLiveData<List<Product>>()
    val profileStatusLiveData = SingleLiveEvent<Int>()
    val burnEventInProgress = MutableLiveData<BurnEvent?>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        firstLaunchPopulateDB()
        viewModelScope.launch(Dispatchers.IO) {
            this.launch(Dispatchers.IO) {
                interactor.getBurnEventsByStatusFlow(Constants.BURN_EVENT_STATUS_IN_PROGRESS).collect {
                    if (it.isNotEmpty()) {
                        burnEventInProgress.postValue(it[0])
                    } else {
                        burnEventInProgress.postValue(null)
                    }
                }
            }

            this.launch(Dispatchers.IO) {
                getProductsToBurnFlow().collect {
                    println("listtoBurn2 = ${it.size}")
                    burnListLiveData.postValue(it)
                }
            }
        }
    }

    fun startBurn() {
        val burnEvent = BurnEvent(
            productsId = interactor.getBurnList(),
            caloriesBurned = 0
        )
        startBurn(burnEvent)
    }

    private fun startBurn(burnEvent: BurnEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            if (interactor.startBurnEvent(burnEvent)) {
                profileStatusLiveData.postValue(Constants.PROFILE_IS_FILLED)
            } else {
                profileStatusLiveData.postValue(Constants.PROFILE_IS_NOT_FILLED)
            }
        }
    }

    fun getProductsToBurnFlow() = interactor.getProductsToBurnStateFlow()

    private fun isBurnEventInProgress(burnEvent: BurnEvent?): Boolean {
        return (burnEvent!=null && burnEvent.eventStatus==Constants.BURN_EVENT_STATUS_IN_PROGRESS)
    }

    private fun firstLaunchPopulateDB() {
        viewModelScope.launch(Dispatchers.IO) {
            if (interactor.getProducts().isEmpty()) {
                val prePopulateDB = PrePopulateDB()
                interactor.saveProducts(prePopulateDB.getPrepolulateProducts())
            }
        }
    }
}