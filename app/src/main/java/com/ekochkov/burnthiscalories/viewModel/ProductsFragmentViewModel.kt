package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.ViewModel
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.domain.Interactor
import javax.inject.Inject

class ProductsFragmentViewModel: ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun getProductsFlow() = interactor.getProductsFlow()

    fun addProductInBurnList(product: Product) {
        interactor.addProductToBurnList(product)
    }
}