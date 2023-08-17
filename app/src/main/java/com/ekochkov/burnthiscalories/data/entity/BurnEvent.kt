package com.ekochkov.burnthiscalories.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase
import com.ekochkov.burnthiscalories.util.Constants
import java.io.Serializable

@Entity(tableName = AppDataBase.BURN_EVENT_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class BurnEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productsId: List<Product>,
    @ColumnInfo(name = "caloriesBurned") val caloriesBurned: Int,
    @ColumnInfo(name = "eventStatus") val eventStatus: Int = Constants.BURN_EVENT_STATUS_IN_PROGRESS,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Product)!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeTypedList(productsId)
        parcel.writeInt(caloriesBurned)
        parcel.writeInt(eventStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BurnEvent> {
        override fun createFromParcel(parcel: Parcel): BurnEvent {
            return BurnEvent(parcel)
        }

        override fun newArray(size: Int): Array<BurnEvent?> {
            return arrayOfNulls(size)
        }
    }
}