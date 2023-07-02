package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.domain.Interactor
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel: ViewModel() {

    val profileLiveData = MutableLiveData<Profile?>()
    val burnEventInProgress = MutableLiveData<Boolean>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            profileLiveData.postValue(interactor.getProfile())
            checkOnBurnEventInProgress()
        }
    }

    private suspend fun checkOnBurnEventInProgress() {
        val burnEvent = interactor.getBurnEventInProgress()
        if (isBurnEventInProgress(burnEvent)) {
            burnEventInProgress.postValue(true)
            interactor.resumeBurnEvent(burnEvent!!)
        }
    }

    private fun isBurnEventInProgress(burnEvent: BurnEvent?): Boolean {
        return (burnEvent!=null && burnEvent.eventStatus== Constants.BURN_EVENT_STATUS_IN_PROGRESS)
    }
}