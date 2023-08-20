package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel: ViewModel() {


    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun tryLaunchStepCount() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getBurnEventInProgress()?.let {
                interactor.resumeBurnEvent(it)
            }
        }
    }
}