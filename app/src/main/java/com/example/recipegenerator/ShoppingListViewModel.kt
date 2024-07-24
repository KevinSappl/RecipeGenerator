package com.example.recipegenerator

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class ShoppingListViewModel : ViewModel()  {
    private var itemId = 0
    val shoppingItems = mutableStateListOf<ShoppingItem>()

    fun addItem(name: String, quantity: String, details: String = "") {
        shoppingItems.add(ShoppingItem(id = itemId++, name = name, quantity = quantity, details = details))
    }

    fun removeItem(item: ShoppingItem) {
        shoppingItems.remove(item)
    }
}