package com.ekochkov.burnthiscalories.view.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.ItemProductBinding
import com.ekochkov.burnthiscalories.util.OnItemClickListener

class ProductHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, clickListener: OnItemClickListener<Product>) {
        binding.name.text = product.name
        binding.description.text = product.description
        val calory = "${product.calory} ккал"
        binding.calory.text = calory
        binding.root.setOnClickListener {
            clickListener.onItemClick(product)
        }
    }
}