package com.ekochkov.burnthiscalories.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.databinding.ItemProductBinding
import com.ekochkov.burnthiscalories.util.OnItemClickListener
import com.ekochkov.burnthiscalories.view.holders.ProductHolder

class ProductListAdapter(private val clickListener: OnItemClickListener<Product>): RecyclerView.Adapter<ProductHolder>() {

    val products = arrayListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(products[position], clickListener)
    }

    override fun getItemCount(): Int {
        return products.size
    }


}