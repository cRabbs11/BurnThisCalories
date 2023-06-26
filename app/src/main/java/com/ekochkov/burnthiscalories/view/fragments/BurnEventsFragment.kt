package com.ekochkov.burnthiscalories.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.databinding.FragmentBurnEventsBinding
import com.ekochkov.burnthiscalories.diffs.BurnEventDiff
import com.ekochkov.burnthiscalories.util.OnItemClickListener
import com.ekochkov.burnthiscalories.view.adapters.BurnEventListAdapter
import com.ekochkov.burnthiscalories.viewModel.BurnEventsFragmentViewModel
import kotlinx.coroutines.launch

class BurnEventsFragment: Fragment() {

    private lateinit var binding: FragmentBurnEventsBinding
    private lateinit var adapter: BurnEventListAdapter
    val viewModel: BurnEventsFragmentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBurnEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BurnEventListAdapter(object: OnItemClickListener<BurnEvent> {
            override fun onItemClick(burnEvent: BurnEvent) {
                val bundle = Bundle()
                bundle.putString("arg1", "value1")
                val actionToBurnEvent = BurnEventsFragmentDirections.actionBurnEventsFragmentToBurnEventFragment()
                actionToBurnEvent.burnEventId = burnEvent.id
                findNavController().navigate(actionToBurnEvent)
            }
        })
        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        lifecycleScope.launch {
            viewModel.getBurnEventsFlow().collect {
                updateRecyclerView(it)
            }
        }
    }

    private fun updateRecyclerView(burnEvents: List<BurnEvent>) {
        val diff = BurnEventDiff(adapter.burnEventList, burnEvents)
        val diffResult = DiffUtil.calculateDiff(diff)
        adapter.burnEventList.clear()
        adapter.burnEventList.addAll(burnEvents)
        diffResult.dispatchUpdatesTo(adapter)
    }
}