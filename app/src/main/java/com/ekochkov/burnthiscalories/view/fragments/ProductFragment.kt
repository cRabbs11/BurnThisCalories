package com.ekochkov.burnthiscalories.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.databinding.FragmentProductBinding
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.viewModel.ProductFragmentViewModel
import com.ekochkov.burnthiscalories.viewModel.factory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductFragment: Fragment() {

    lateinit var binding: FragmentProductBinding
    private val viewModel: ProductFragmentViewModel by viewModels { factory(productId = arguments?.getInt(getString(R.string.argument_product_id)))}

    private var productJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameEditText.addTextChangedListener { text ->
            text?.let {
                viewModel.checkProductName(it.toString())
            }
        }

        binding.acceptBtn.setOnClickListener {
            if (isProductFilled()) {
                val name = binding.nameEditText.text.toString()
                val description = binding.descriptionEditText.text.toString()
                val calory = binding.caloryEditText.text.toString().toInt()
                viewModel.addProduct(name, description, calory)
            } else {
                showToast(Constants.FIELDS_NOT_FILLED)
            }
        }

        binding.deleteBtn.setOnClickListener {
            if (viewModel.getProductId()!=Constants.NO_PRODUCT_ID) {
                viewModel.deleteProduct()
            }
        }

        startProductFlow()

        viewModel.isNameNotExistLiveData.observe(viewLifecycleOwner) { isNameNotExist ->
            binding.acceptBtn.isEnabled = isNameNotExist
            binding.nameEditTextField.isErrorEnabled = !isNameNotExist
            if (!isNameNotExist) {
                binding.nameEditTextField.error = getString(R.string.name_aleady_exist)
            }
        }

        viewModel.toastLiveData.observe(viewLifecycleOwner) {
            showToast(it)
        }

        viewModel.dbResultLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                DB_RESULT_SAVED_SUCCESS -> {
                    showToast(Constants.PRODUCT_ADDED)
                    startProductFlow()
                }
                DB_RESULT_SAVED_ERROR -> {
                    showToast(Constants.PRODUCT_ADDED_ERROR)
                    binding.acceptBtn.isEnabled = true
                }
                DB_RESULT_UPDATED_ERROR -> {
                    showToast(Constants.PRODUCT_UPDATED_ERROR)
                    binding.acceptBtn.isEnabled = true
                }
                DB_RESULT_UPDATED_SUCCESS -> {
                    showToast(Constants.PRODUCT_UPDATED)
                    binding.acceptBtn.isEnabled = true
                }
                DB_RESULT_DELETED_SUCCESS -> {
                    showToast(Constants.PRODUCT_DELETED)
                    findNavController().popBackStack()
                }
                DB_RESULT_DELETED_ERROR -> {
                    showToast(Constants.DB_RESULT_ERROR)
                }
            }
        }
    }

    private fun isProductFilled(): Boolean {
        return !(binding.nameEditText.text.isNullOrEmpty() &&
                binding.descriptionEditText.text.isNullOrEmpty() &&
                binding.caloryEditText.text.isNullOrEmpty())
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val DB_RESULT_SAVED_SUCCESS = 0
        const val DB_RESULT_SAVED_ERROR = 1
        const val DB_RESULT_UPDATED_SUCCESS = 2
        const val DB_RESULT_UPDATED_ERROR = 3
        const val DB_RESULT_DELETED_SUCCESS = 4
        const val DB_RESULT_DELETED_ERROR = 5
    }

    private fun startProductFlow() {
        productJob?.cancel()
        productJob = lifecycle.coroutineScope.launch {
            viewModel.getProductFlow(viewModel.getProductId()).collect { product ->
                product?.let {
                    binding.nameEditText.setText(it.name)
                    binding.descriptionEditText.setText(it.description)
                    binding.caloryEditText.setText(it.calory.toString())
                }
            }
        }
    }
}