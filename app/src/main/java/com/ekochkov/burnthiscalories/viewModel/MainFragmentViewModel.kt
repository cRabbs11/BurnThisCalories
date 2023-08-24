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
    val burnEventInProgress = MutableLiveData<BurnEvent?>()
    val toastLiveData = SingleLiveEvent<String>()

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
                interactor.getProductsToBurnStateFlow().collect {
                    println("listtoBurn2 = ${it.size}")
                    burnListLiveData.postValue(it)
                }
            }
        }
    }

    fun startBurn() {
        viewModelScope.launch(Dispatchers.Default) {
            if (interactor.isProfileExist()) {
                val burnlist = burnListLiveData.value
                    if (burnlist!=null && burnlist.isNotEmpty()) {
                        interactor.startBurnEvent()
                    } else {
                        toastLiveData.postValue(Constants.BURNLIST_IS_NOT_FILLED_TEXT)
                    }
            } else {
                toastLiveData.postValue(Constants.PROFILE_IS_NOT_FILLED_TEXT)
            }
        }
    }

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