package com.ekochkov.burnthiscalories.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase
import java.io.Serializable

@Entity(tableName = AppDataBase.PRODUCT_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "calory") val calory: Int,
    @ColumnInfo(name = "isCustom") val isCustom: Boolean,
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(category)
        parcel.writeString(description)
        parcel.writeInt(calory)
        parcel.writeByte(if (isCustom) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        val CATEGORY_FOOD = 1
        val CATEGORY_DRINK = 2
        val CATEGORY_OTHER = 3

        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}

