package com.ekochkov.burnthiscalories.di

import com.ekochkov.burnthiscalories.di.modules.DataModule
import com.ekochkov.burnthiscalories.di.modules.DomainModule
import com.ekochkov.burnthiscalories.viewModel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DataModule::class,
    DomainModule::class
])
interface AppComponent {

    fun inject(launchFragmentViewModel: ProfileFragmentViewModel)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(productsFragmentViewModel: ProductsFragmentViewModel)
    fun inject(burnEventsFragmentViewModel: BurnEventsFragmentViewModel)
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(newProductViewModel: ProductFragmentViewModel)
    fun inject(burnEventFragmentViewModel: BurnEventFragmentViewModel)
}