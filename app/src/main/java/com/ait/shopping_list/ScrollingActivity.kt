package com.ait.shopping_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.ait.shopping_list.adapter.ItemAdapter
import com.ait.shopping_list.data.AppDatabase
import com.ait.shopping_list.data.ShopItem
import com.ait.shopping_list.touch.ItemRecyclerTouchCallback
import com.ait.shopping_list.touch.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.activity_scrolling.toolbar
import kotlinx.android.synthetic.main.item_dialog.*
import kotlinx.android.synthetic.main.row.view.*

class ScrollingActivity : AppCompatActivity(), Dialog.Handler, AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this, parent?.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
    }

    lateinit var adapter: ItemAdapter
    var editIndex: Int = -1

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
        const val TAG_TODO_DIALOG = "TAG_TODO_DIALOG"
        const val TAG_ITEM_EDIT = "TAG_ITEM_EDIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showDialog()
        }

        fabDeleteAll.setOnClickListener {
            adapter.deleteAllItems()
        }

        initReyclerView()
    }

    fun initReyclerView() {
        Thread {
            var items = AppDatabase.getInstance(this@ScrollingActivity).ItemDao().getALlItems()
            runOnUiThread {
                adapter = ItemAdapter(this, items)
                recyclerItem.adapter = adapter
                var itemDecorator = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
                recyclerItem.addItemDecoration(itemDecorator)
                val callback = ItemRecyclerTouchCallback(adapter)
                val touchhelper = ItemTouchHelper(callback)
                touchhelper.attachToRecyclerView(recyclerItem)
            }
        }.start()
    }
    fun showDialog() {
        Dialog().show(supportFragmentManager, "Tag_todo_dialog")
    }

    fun showEditDialog(item: ShopItem, index : Int ) {
        editIndex = index
        val editDialog = Dialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, item)
        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager, TAG_ITEM_EDIT)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun itemCreated(item: ShopItem) {
          saveItem(item)
    }

    override fun itemUpdated(item: ShopItem) {
        Thread {
            AppDatabase.getInstance(this@ScrollingActivity).ItemDao().updateItem(item)
            runOnUiThread {
               adapter.updateItemOnPosition(item, editIndex)
            }
        }.start()
    }

    fun saveItem(item: ShopItem) {
        Thread {
            var newId = AppDatabase.getInstance(this@ScrollingActivity).ItemDao().addItem(item)
            item.itemId = newId
            runOnUiThread {
                adapter.addItem(item)
            }
        }.start()
    }
}
