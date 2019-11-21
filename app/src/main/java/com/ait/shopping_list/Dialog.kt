package com.ait.shopping_list

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ait.shopping_list.ScrollingActivity.Companion.KEY_ITEM
import com.ait.shopping_list.data.ShopItem
import kotlinx.android.synthetic.main.item_dialog.*
import kotlinx.android.synthetic.main.item_dialog.view.*
import java.util.*

class Dialog : DialogFragment() {
    interface Handler {
        fun itemCreated(ShopItem: ShopItem)
        fun itemUpdated(ShopItem: ShopItem)
    }

    private lateinit var handler : Handler
    var isEditMode = false
    private lateinit var etItemName: EditText
    private lateinit var etItemPrice: EditText
    private lateinit var etItemDes: EditText
    private lateinit var etItemStatus: CheckBox
    private lateinit var categorySpinner : Spinner


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Handler) {
            handler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the HandlerInterface")
        }
    }

    fun showTitle(builder : AlertDialog.Builder) {
        if (isEditMode) {
            builder.setTitle("Edit Item")
        } else {
            builder.setTitle("New Item")
        }
    }

    private fun handleErrorMes() {
        if (etItemName.text.isEmpty()){
            etItemName.error = getString(R.string.error_mes)
        }
        if (etItemPrice.text.isEmpty()) {
            etItemPrice.error = getString(R.string.error_mes)
        }

        if (etItemStatus.text.isEmpty()) {
            etItemStatus.error = getString(R.string.error_mes)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // build dialog
        val builder = AlertDialog.Builder(requireContext())

        /* If arguments is not null the dialog object has arguments (kotlin call getarguments) */
        isEditMode = ((arguments != null) && arguments!!.containsKey(KEY_ITEM))

        showTitle(builder)
        /* create separate layout file for dialog */
        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.item_dialog, null
        )
        categorySpinner = rootView.category_spinner
        etItemName = rootView.etItemName
        etItemPrice = rootView.etPrice
        etItemDes = rootView.etDescribe
        etItemStatus = rootView.status

        builder.setView(rootView)

        var cateAdapter =
            ArrayAdapter.createFromResource(context as ScrollingActivity, R.array.category_array, android.R.layout.simple_spinner_item)
        cateAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = cateAdapter

        if (isEditMode) {
            var shopItem : ShopItem = (arguments?.getSerializable(KEY_ITEM) as ShopItem)
            etItemName.setText(shopItem.name)
            etItemPrice.setText(shopItem.price.toString())
            etItemDes.setText(shopItem.descript)
            etItemStatus.setText(shopItem.status.toString())
            categorySpinner.setSelection(shopItem.category)
        }

        builder.setPositiveButton("OK") {
                dialog, witch -> // empty
        }

        return builder.create()
    }


    /*
    override fun onResume() {
        super.onResume()


        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etItemName.text.isNotEmpty()) {
                if (isEditMode) {
                    handleEditItem()
                } else {
                    handleCreateItem()
                }
                // if there is sth in the text then we create a new todo
                dialog?.dismiss()
            } else {
                etItemName.error = "This field can not be empty"
            }
        }


    }
    */


    private fun checkFields() : Boolean{
        return etItemName.text.isNotEmpty() && etItemPrice.text.isNotEmpty() && etItemStatus.text.isNotEmpty()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (checkFields()) {
                if (isEditMode) {
                    handleEditItem()
                } else {
                    handleCreateItem()
                }
                dialog?.dismiss()
            } else {
                handleErrorMes()
            }
        }
    }




    private fun handleEditItem() {
        val itemToEdit = arguments?.getSerializable(KEY_ITEM) as ShopItem
        itemToEdit.category = categorySpinner.selectedItemPosition
        itemToEdit.name = etItemName.text.toString()
        itemToEdit.descript = etItemDes.text.toString()
        itemToEdit.price = etItemPrice.text.toString().toFloat()
        itemToEdit.status = etItemStatus.isChecked()
        handler.itemUpdated(itemToEdit)

    }
    fun handleCreateItem() {
        handler.itemCreated (
            ShopItem(
                null, etItemName.text.toString(),
                categorySpinner.selectedItemPosition, etItemStatus.isChecked,
                etItemPrice.text.toString().toFloat(),
                etItemDes.text.toString()
            )
        )
    }
}