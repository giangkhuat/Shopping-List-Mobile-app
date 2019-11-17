package com.ait.shopping_list.touch

interface ItemTouchHelperCallback {
        fun onDismissed(position: Int)
        fun onItemMoved(fromPosition: Int, toPosition: Int)
}