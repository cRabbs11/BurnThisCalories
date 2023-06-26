package com.ekochkov.burnthiscalories.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.ItemBurnEventBinding
import com.ekochkov.burnthiscalories.util.OnItemClickListener
import com.ekochkov.burnthiscalories.view.holders.BurnEventHolder

class BurnEventListAdapter(private val clickListener: OnItemClickListener<BurnEvent>): RecyclerView.Adapter<BurnEventHolder>() {

    val burnEventList = arrayListOf<BurnEvent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurnEventHolder {
        val binding = ItemBurnEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BurnEventHolder(binding)
    }

    override fun onBindViewHolder(holder: BurnEventHolder, position: Int) {
        holder.bind(burnEventList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return burnEventList.size
    }
}