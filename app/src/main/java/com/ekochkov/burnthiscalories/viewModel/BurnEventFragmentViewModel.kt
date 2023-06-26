package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BurnEventFragmentViewModel(private val burnEventId: Int): ViewModel() {

    val burnEventLiveData = MutableLiveData<BurnEvent>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getBurnEventFlow(burnEventId).collect{
                burnEventLiveData.postValue(it)
            }
        }
    }

    fun finishEvent() {
        interactor.finishEvent()
    }
}