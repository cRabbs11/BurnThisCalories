package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel: ViewModel() {

    val profileLiveData = MutableLiveData<Profile?>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            profileLiveData.postValue(interactor.getProfile())
        }
    }

    fun tryLaunchStepCount() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getBurnEventInProgress()?.let {
                interactor.resumeBurnEvent(it)
            }
        }
    }

    fun stopLaunchStepCount() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.stopBurnEvent()
        }
    }
}