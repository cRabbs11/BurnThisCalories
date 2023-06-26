package com.ekochkov.burnthiscalories.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.FragmentBurnEventBinding
import com.ekochkov.burnthiscalories.diffs.ProductDiff
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.util.OnItemClickListener
import com.ekochkov.burnthiscalories.view.adapters.ProductListAdapter
import com.ekochkov.burnthiscalories.viewModel.BurnEventFragmentViewModel
import com.ekochkov.burnthiscalories.viewModel.factory

class BurnEventFragment: Fragment() {

    private lateinit var productAdapter: ProductListAdapter
    lateinit var binding: FragmentBurnEventBinding
    private val viewModel: BurnEventFragmentViewModel by viewModels { factory(burnEventId = arguments?.getInt(getString(
        R.string.argument_burn_event_id)))}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBurnEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductListAdapter(object: OnItemClickListener<Product> {
            override fun onItemClick(t: Product) {

            }
        })
        binding.recyclerView.adapter = productAdapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.burnEventLiveData.observe(viewLifecycleOwner) { burnEvent ->
            if (burnEvent.eventStatus==Constants.BURN_EVENT_STATUS_IN_PROGRESS) {
                binding.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_white_yellow)
                binding.burnEventStatusTextView.text = getString(R.string.burn_event_status_in_progress)
            } else if (burnEvent.eventStatus==Constants.BURN_EVENT_STATUS_DONE) {
                binding.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_white_green)
                binding.burnEventStatusTextView.text = getString(R.string.burn_event_status_done)
            }

            var caloriesAll = 0
            burnEvent.productsId.forEach {
                caloriesAll+=it.calory
            }
            val caloriesLeftText = "калорий осталось: ${caloriesAll-burnEvent.caloriesBurned}"
            binding.caloriesLeftTextView.text = caloriesLeftText

            updateRecyclerView(burnEvent.productsId)
        }

        binding.finishBtn.setOnClickListener {
            viewModel.finishEvent()
        }
    }

    private fun updateRecyclerView(products: List<Product>) {
        val diff = ProductDiff(productAdapter.products, products)
        val diffResult = DiffUtil.calculateDiff(diff)
        productAdapter.products.clear()
        productAdapter.products.addAll(products)
        diffResult.dispatchUpdatesTo(productAdapter)
    }
}