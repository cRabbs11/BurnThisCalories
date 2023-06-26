package com.ekochkov.burnthiscalories.diffs

import androidx.recyclerview.widget.DiffUtil
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product

class BurnEventDiff(val oldList: List<BurnEvent>, val newList: List<BurnEvent>): DiffUtil.Callback() {
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
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id==new.id &&
                old.caloriesBurned==new.caloriesBurned &&
                old.eventStatus==new.eventStatus
    }
}