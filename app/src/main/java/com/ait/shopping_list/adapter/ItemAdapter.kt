package com.ait.shopping_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ait.shopping_list.R
import com.ait.shopping_list.ScrollingActivity
import com.ait.shopping_list.data.AppDatabase
import com.ait.shopping_list.data.ShopItem
import com.ait.shopping_list.touch.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.row.view.*
import java.util.*
import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.ait.shopping_list.Dialog
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.row.view.status


class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>, ItemTouchHelperCallback {

    var shoplist  = mutableListOf<ShopItem>()
    var context: Context

    constructor(context: Context, items: List<ShopItem>) {
        this.context = context
        shoplist.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* define layout of one ShopItem  outside actiity */
        var item = LayoutInflater.from(context).inflate(
            R.layout.row, parent, false
        )
        return ViewHolder(item)
    }
    override fun getItemCount(): Int {
        return shoplist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = shoplist.get(holder.adapterPosition)
        holder.product.text = item.name
        holder.describe.text = item.descript
        holder.price.text = item.price.toString()
        holder.status.isChecked = item.status
        holderButtons(holder, item)
        showIconCategory(item.category, holder)
    }

    fun showIconCategory(category : Int, holder : ViewHolder) {
        if (category == 0) {
            holder.icon.setImageResource(R.drawable.food)
        } else if (category == 1) {
            holder.icon.setImageResource(R.drawable.clothes_icon)
        } else if (category == 2) {
            holder.icon.setImageResource(R.drawable.electronics)
        }
    }


    fun holderButtons(holder : ViewHolder, item : ShopItem) {
        holder.btnDelete.setOnClickListener() {
            deleteItem(holder.adapterPosition)
        }

        holder.status.setOnClickListener() {
            item.status = holder.status.isChecked
            updateItem(item)
        }

        holder.edit.setOnClickListener() {
            (context as ScrollingActivity).showEditDialog(item, holder.adapterPosition)
        }


        holder.btnView.setOnClickListener {
            (context as ScrollingActivity).showEditDialog(item, holder.adapterPosition)
        }



    }

    fun deleteItem(index: Int) {
        Thread {
            AppDatabase.getInstance(context).ItemDao().deleteItem(shoplist[index])
            (context as ScrollingActivity).runOnUiThread()  {
                shoplist.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun addItem(ShopItem : ShopItem) {
        shoplist.add(ShopItem)
        notifyItemInserted(shoplist.lastIndex)
    }

    fun deleteAllItems() {
        Thread {
            AppDatabase.getInstance(context).ItemDao().deleteAll()
            (context as ScrollingActivity).runOnUiThread {
                shoplist.clear()
                notifyDataSetChanged()
            }
        }.start()
    }


    /* Update recyclerview */
    fun updateItemOnPosition(item: ShopItem, index: Int) {
        shoplist.set(index, item)
        notifyItemChanged(index)
    }

    /* Update database */
    fun updateItem(item: ShopItem) {
        Thread {
            AppDatabase.getInstance(context).ItemDao().updateItem(item)
        }.start()

    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoplist, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon = itemView.icon
        var product = itemView.product_name
        var category = itemView.category
        var price = itemView.price
        var describe = itemView.describe
        var status = itemView.status
        var btnDelete = itemView.btnDelete
        var edit = itemView.btnEdit
        var btnView = itemView.expand
    }

}