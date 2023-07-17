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

class ProfileFragmentViewModel: ViewModel() {

    val prodileLiveData = MutableLiveData<Profile>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            getProfileFlow().collect { profile ->
                profile?.let {
                    prodileLiveData.postValue(profile)
                }
            }
        }
    }

    fun saveProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveProfile(profile)
        }
    }

    private fun getProfileFlow() = interactor.getProfileFlow()
}