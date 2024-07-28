package com.example.recipegenerator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val shoppingItemDao: ShoppingItemDao
    val shoppingItems: LiveData<List<ShoppingItem>>

    init {
        val database = ShoppingListDatabase.getDatabase(application)
        shoppingItemDao = database.shoppingItemDao()
        shoppingItems = shoppingItemDao.getAll()
    }

    fun addItem(name: String, quantity: String, details: String = "") {
        viewModelScope.launch {
            shoppingItemDao.insert(ShoppingItem(name = name, quantity = quantity, details = details))
        }
    }

    fun removeItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingItemDao.delete(item)
        }
    }
}