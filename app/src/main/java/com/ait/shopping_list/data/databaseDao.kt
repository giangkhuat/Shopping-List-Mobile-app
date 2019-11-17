package com.ait.shopping_list.data

import androidx.room.*

@Dao
interface databaseDao {
    @Query("Select * from shopItem")
    fun getALlItems() : List<ShopItem>

    @Insert
    fun addItem(shopItem: ShopItem) : Long

    @Delete
    fun deleteItem(shopItem: ShopItem)

    @Update
    fun updateItem(shopItem: ShopItem)

    @Query("DELETE from shopItem")
    fun deleteAll()
}