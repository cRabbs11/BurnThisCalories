package com.ekochkov.burnthiscalories.view.holders

import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.ItemBurnEventBinding
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.util.OnItemClickListener

class BurnEventHolder(val binding: ItemBurnEventBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(burnEvent: BurnEvent, clickListener: OnItemClickListener<BurnEvent>) {
        binding.status.text = when(burnEvent.eventStatus) {
            Constants.BURN_EVENT_STATUS_DONE -> Constants.BURN_EVENT_STATUS_DONE_TEXT
            Constants.BURN_EVENT_STATUS_IN_PROGRESS -> Constants.BURN_EVENT_STATUS_IN_PROGRESS_TEXT
            else -> {Constants.BURN_EVENT_STATUS_NOT_FOUND_TEXT}
        }
        val caloriesBurned = "сожжено калорий: ${burnEvent.caloriesBurned}"
        val countOfProducts = "продуктов: ${burnEvent.productsId.size}"
        binding.caloriesBurned.text = caloriesBurned
        binding.countOfProducts.text = countOfProducts

        binding.root.setOnClickListener {
            clickListener.onItemClick(burnEvent)
        }
    }
}