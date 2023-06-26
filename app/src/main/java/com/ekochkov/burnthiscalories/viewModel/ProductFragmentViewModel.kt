package com.ekochkov.burnthiscalories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekochkov.burnthiscalories.App
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.domain.Interactor
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.util.SingleLiveEvent
import com.ekochkov.burnthiscalories.view.fragments.ProductFragment.Companion.DB_RESULT_DELETED_ERROR
import com.ekochkov.burnthiscalories.view.fragments.ProductFragment.Companion.DB_RESULT_DELETED_SUCCESS
import com.ekochkov.burnthiscalories.view.fragments.ProductFragment.Companion.DB_RESULT_SAVED_ERROR
import com.ekochkov.burnthiscalories.view.fragments.ProductFragment.Companion.DB_RESULT_SAVED_SUCCESS
import com.ekochkov.burnthiscalories.view.fragments.ProductFragment.Companion.DB_RESULT_UPDATED_ERROR
import com.ekochkov.burnthiscalories.view.fragments.ProductFragment.Companion.DB_RESULT_UPDATED_SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductFragmentViewModel(private var productId: Int): ViewModel() {

    val toastLiveData = SingleLiveEvent<String>()

    val dbResultLiveData = SingleLiveEvent<Int>()
    val isNameNotExistLiveData = SingleLiveEvent<Boolean>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        println("productId = $productId")
    }

    //fun addNewProduct(product: Product) {
    //    viewModelScope.launch(Dispatchers.IO) {
    //        if (productId==Constants.NO_PRODUCT_ID) {
    //            val productFromDB = interactor.getProduct(product.name)
    //            if (productFromDB==null) {
    //                interactor.saveProduct(product)
    //                toastLiveData.postValue(Constants.PRODUCT_ADDED)
    //            } else {
    //                toastLiveData.postValue(Constants.PRODUCT_WITH_NAME_ALREADY_EXIST)
    //            }
    //        } else {
    //            interactor.saveProduct(product)
    //            toastLiveData.postValue(Constants.PRODUCT_UPDATED)
    //        }
    //    }
    //}

    fun addProduct(name: String, description: String, calory: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productId==Constants.NO_PRODUCT_ID) {
                val result = interactor.saveProduct(Product(
                    name = name,
                    category = Product.CATEGORY_FOOD,
                    description = description,
                    calory = calory,
                    isCustom = true))
                if (result>0) {
                    dbResultLiveData.postValue(DB_RESULT_SAVED_SUCCESS)
                    productId = result.toInt()
                } else {
                    dbResultLiveData.postValue(DB_RESULT_SAVED_ERROR)
                }
            } else {
                val result = interactor.updateProduct(Product(
                    id = productId,
                    name = name,
                    category = Product.CATEGORY_FOOD,
                    description = description,
                    calory = calory,
                    isCustom = true))
                if (result==1) {
                    dbResultLiveData.postValue(DB_RESULT_UPDATED_SUCCESS)
                } else {
                    dbResultLiveData.postValue(DB_RESULT_UPDATED_ERROR)
                }
            }
        }
    }

    fun deleteProduct() {
        if (productId!=Constants.NO_PRODUCT_ID) {
            viewModelScope.launch(Dispatchers.IO) {
                interactor.getProduct(productId)?.let { product ->
                    val result = interactor.deleteProduct(product)
                    if (result==1) {
                        dbResultLiveData.postValue(DB_RESULT_DELETED_SUCCESS)
                    } else {
                        dbResultLiveData.postValue(DB_RESULT_DELETED_ERROR)
                    }
                }
            }
        }
    }

    private suspend fun isNameNotExist(name: String): Boolean {
        val productFromDB = interactor.getProduct(name)
        return productFromDB == null
    }

    fun getProductFlow(id: Int) = interactor.getProductFlow(id)

    fun checkProductName(name: String) {
        if (productId!=Constants.NO_PRODUCT_ID) {
            viewModelScope.launch(Dispatchers.IO) {
                val product = interactor.getProduct(productId)
                val result = isNameNotExist(name)
                if (result) {
                    isNameNotExistLiveData.postValue(true)
                } else if (name==product?.name) {
                    isNameNotExistLiveData.postValue(true)
                } else {
                    isNameNotExistLiveData.postValue(false)
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val result = isNameNotExist(name)
                isNameNotExistLiveData.postValue(result)
            }
        }
    }

    fun getProductId(): Int {
        return productId
    }
}