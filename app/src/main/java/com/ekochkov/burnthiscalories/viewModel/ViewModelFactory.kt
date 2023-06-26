package com.ekochkov.burnthiscalories.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekochkov.burnthiscalories.util.Constants

class ViewModelFactory(private val productId: Int?, private val burnEventId: Int?): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ProductFragmentViewModel::class.java -> {
                if (productId!=null) {
                    ProductFragmentViewModel(productId)
                } else {
                    throw IllegalStateException(Constants.EXCEPTION_MESSAGE_ARGUMENT_IS_NULL)
                }
            }
            BurnEventFragmentViewModel::class.java -> {
                if (burnEventId!=null) {
                    BurnEventFragmentViewModel(burnEventId)
                } else {
                    throw IllegalStateException(Constants.EXCEPTION_MESSAGE_ARGUMENT_IS_NULL)
                }
            }
            else -> {
                throw IllegalStateException(Constants.EXCEPTION_MESSAGE_UNKNOWN_VIEW_MODEL)
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory(productId: Int? = null, burnEventId: Int? = null) =
    ViewModelFactory(productId = productId, burnEventId = burnEventId)