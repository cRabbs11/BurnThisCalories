package com.ekochkov.burnthiscalories.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.FragmentMainBinding
import com.ekochkov.burnthiscalories.diffs.ProductDiff
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.util.OnItemClickListener
import com.ekochkov.burnthiscalories.view.adapters.ProductListAdapter
import com.ekochkov.burnthiscalories.view.fragments.ProductsFragment.Companion.FLAG_ADD_PRODUCTS_TO_BURN_EVENT
import com.ekochkov.burnthiscalories.viewModel.MainFragmentViewModel

class MainFragment: Fragment() {

    lateinit var binding : FragmentMainBinding
    lateinit var productAdapter: ProductListAdapter
    val viewModel: MainFragmentViewModel by viewModels()
    private var burnEventStatus = Constants.BURN_EVENT_STATUS_DONE
    private var burnEventInProgressId = -1

    private val permissionLocationLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (checkPermission(Manifest.permission.ACTIVITY_RECOGNITION)) {
            viewModel.startBurn()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    //TODO отслеживать состояние профиля

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)

        viewModel.toastLiveData.observe(viewLifecycleOwner) { text ->
            showToast(text)
        }

        productAdapter = ProductListAdapter(object: OnItemClickListener<Product> {
            override fun onItemClick(product: Product) {
                viewModel.removeProductFromBurnList(product)
            }
        })
        binding.recyclerView.adapter = productAdapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        //binding.runTypeMenuTextView.setAdapter(adapter)

        viewModel.burnListLiveData.observe(viewLifecycleOwner) {
            Log.d("BMTH", "products: ${it.size}")
            updateRecyclerView(it)
            if (it.isNotEmpty()) {
                var allCalories = 0
                it.forEach { product ->
                    allCalories+=product.calory
                }
                val allCaloriesText = "${allCalories} ккал"
                binding.allCalories.text = allCaloriesText
                binding.allCalories.visibility = View.VISIBLE
            } else {
                binding.allCalories.visibility = View.GONE
            }
        }

        binding.addProductBtn.setOnClickListener {
            val actionToProducts = MainFragmentDirections.actionMainFragmentToProductsFragment()
            actionToProducts.flag = FLAG_ADD_PRODUCTS_TO_BURN_EVENT
            findNavController().navigate(actionToProducts)
        }

        binding.startBtn.setOnClickListener {
            requestPermission(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        binding.toBurnEventBtn.setOnClickListener {
            if (burnEventInProgressId!=-1) {
                val actionToBurnEvent = MainFragmentDirections.actionMainFragmentToBurnEventFragment()
                actionToBurnEvent.burnEventId = burnEventInProgressId
                findNavController().navigate(actionToBurnEvent)
            } else {
                showToast(getString(R.string.burn_event_not_found))
            }
        }

        viewModel.burnEventInProgress.observe(viewLifecycleOwner) { burnEvent ->
            burnEventInProgressId = burnEvent?.id ?: -1
            updateButtons(burnEvent)
        }
    }

    private fun updateRecyclerView(products: List<Product>) {
        val diff = ProductDiff(productAdapter.products, products)
        val diffResult = DiffUtil.calculateDiff(diff)
        productAdapter.products.clear()
        productAdapter.products.addAll(products)
        diffResult.dispatchUpdatesTo(productAdapter)
    }

    private fun requestPermission(permission: String) {
        when (permission) {
            Manifest.permission.ACTIVITY_RECOGNITION -> {
                permissionLocationLauncher.launch(permission)
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun updateButtons(burnEvent: BurnEvent?) {
        if (burnEvent!=null || burnEventStatus==Constants.BURN_EVENT_STATUS_IN_PROGRESS) {
            binding.startBtn.isEnabled = false
            binding.addProductBtn.isEnabled = false
            binding.toBurnEventBtn.visibility = View.VISIBLE
        } else {
            binding.startBtn.isEnabled = true
            binding.addProductBtn.isEnabled = true
            binding.toBurnEventBtn.visibility = View.GONE
        }
    }
}