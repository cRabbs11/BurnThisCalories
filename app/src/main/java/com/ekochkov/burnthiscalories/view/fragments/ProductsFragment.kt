package com.ekochkov.burnthiscalories.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.FragmentProductsBinding
import com.ekochkov.burnthiscalories.diffs.ProductDiff
import com.ekochkov.burnthiscalories.util.OnItemClickListener
import com.ekochkov.burnthiscalories.view.adapters.ProductListAdapter
import com.ekochkov.burnthiscalories.viewModel.ProductsFragmentViewModel
import kotlinx.coroutines.launch

class ProductsFragment: Fragment() {

    lateinit var binding : FragmentProductsBinding
    lateinit var adapter: ProductListAdapter
    private val viewModel: ProductsFragmentViewModel by viewModels()

    val actionToNewProduct = ProductsFragmentDirections.actionProductsFragmentToNewProductFragment()

    companion object {
        val FLAG_PRODUCT_LIST = 0
        val FLAG_ADD_PRODUCTS_TO_BURN_EVENT = 1
    }

    private var flag = FLAG_PRODUCT_LIST

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getInt(getString(R.string.argument_flag))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (flag) {
            FLAG_ADD_PRODUCTS_TO_BURN_EVENT -> {
                binding.addFab.visibility = View.GONE
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.getProductsFlow().collect { list ->
                updateRecyclerView(list)
            }
        }

        adapter = ProductListAdapter(object : OnItemClickListener<Product> {
            override fun onItemClick(product: Product) {
                when (flag) {
                    FLAG_ADD_PRODUCTS_TO_BURN_EVENT -> {
                        viewModel.addProductInBurnList(product)
                        showToast("product ${product.name} added in burn case")
                    }
                    FLAG_PRODUCT_LIST -> {
                        if (product.isCustom) {
                            val bundle = Bundle()
                            bundle.putString("arg1", "value1")
                            val actionToChangeProduct = ProductsFragmentDirections.actionProductsFragmentToChangeProductFragment()
                            actionToChangeProduct.productId = product.id
                            findNavController().navigate(actionToChangeProduct)
                        } else {
                            showToast("this product cannot changed")
                        }
                    }
                    else -> {
                        showToast("product ${product.name} just displayed")
                    }
                }
            }
        })

        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        binding.addFab.setOnClickListener {
            findNavController().navigate(actionToNewProduct)
        }
    }

    private fun updateRecyclerView(products: List<Product>) {
        val diff = ProductDiff(adapter.products, products)
        val diffResult = DiffUtil.calculateDiff(diff)
        adapter.products.clear()
        adapter.products.addAll(products)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}