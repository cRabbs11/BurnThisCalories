package com.ekochkov.burnthiscalories.view.holders

import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.databinding.ItemBurnEventBinding
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.util.OnItemClickListener

class BurnEventHolder(val binding: ItemBurnEventBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(burnEvent: BurnEvent, clickListener: OnItemClickListener<BurnEvent>) {
        when(burnEvent.eventStatus) {
            Constants.BURN_EVENT_STATUS_DONE -> {
                binding.statusIcon.setImageResource(R.drawable.ic_baseline_done_24)
                binding.statusText.text = Constants.BURN_EVENT_STATUS_DONE_TEXT
            }
            Constants.BURN_EVENT_STATUS_IN_PROGRESS -> {
                binding.statusIcon.setImageResource(R.drawable.ic_baseline_loop_24)
                binding.statusText.text = Constants.BURN_EVENT_STATUS_IN_PROGRESS_TEXT}
            else -> {
                binding.statusIcon.setImageResource(R.drawable.ic_baseline_loop_24)
                binding.statusText.text = Constants.BURN_EVENT_STATUS_NOT_FOUND_TEXT
            }
        }
        val countOfProducts = "${burnEvent.productsId.size} продуктов"
        var calories = 0
        burnEvent.productsId.forEach {
            calories+=it.calory
        }
        val allCalories = "${burnEvent.caloriesBurned}/$calories ккал"

        binding.countOfProducts.text = countOfProducts
        binding.allCalories.text = allCalories

        binding.root.setOnClickListener {
            clickListener.onItemClick(burnEvent)
        }
    }
}