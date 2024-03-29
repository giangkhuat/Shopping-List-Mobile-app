package com.ait.shopping_list.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="shopItem")
data class ShopItem (
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: Int,
    @ColumnInfo(name = "status")var status : Boolean,
    @ColumnInfo(name = "price")var price : Float,
    @ColumnInfo(name = "description")var descript : String
) : Serializable