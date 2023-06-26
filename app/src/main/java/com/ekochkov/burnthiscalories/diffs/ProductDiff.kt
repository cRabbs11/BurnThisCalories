package com.ekochkov.burnthiscalories.diffs

import androidx.recyclerview.widget.DiffUtil
import com.ekochkov.burnthiscalories.data.entity.Product

class ProductDiff(val oldList: List<Product>, val newList: List<Product>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.id==newProduct.id &&
                oldProduct.name==newProduct.name
    }
}