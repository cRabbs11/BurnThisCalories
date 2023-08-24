package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.domain.Interactor
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class BurnEventFragmentViewModel(private val burnEventId: Int): ViewModel() {

    val burnEventLiveData = MutableLiveData<BurnEvent>()
    val isBurnEventServiceCanRun = MutableLiveData<Boolean>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            this.launch {
                combine(interactor.getBurnEventFlow(burnEventId), interactor.getIsBurnEventServiceIsRunningStateFlow()) { burnEvent, isServiceRunning ->
                    burnEventLiveData.postValue(burnEvent)
                    isBurnEventServiceCanRun.postValue(burnEvent?.eventStatus==Constants.BURN_EVENT_STATUS_IN_PROGRESS && !isServiceRunning)
                }.collect()
            }
        }
    }

    fun resumeEvent() {
        GlobalScope.launch(Dispatchers.Default) {
            interactor.resumeBurnEvent(burnEventId)
        }
    }

    fun stopEvent() {
        interactor.stopBurnEvent()
    }
}