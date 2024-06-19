package com.example.recipegenerator

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object GroceryUtils {

    private const val FILE_NAME = "groceries.json" //name of json file where groceries are stored

    fun saveGroceries(context: Context, groceries: List<GroceryItem>) {
        val gson = Gson()
        val jsonString = gson.toJson(groceries)
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(jsonString)
    }

    fun loadGroceries(context: Context): MutableList<GroceryItem> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            return mutableListOf()
        }
        val jsonString = file.readText()
        val gson = Gson()
        val type = object : TypeToken<MutableList<GroceryItem>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun addGrocery(context: Context, grocery: GroceryItem) {
        val groceries = loadGroceries(context)
        groceries.add(grocery)
        saveGroceries(context, groceries)
    }

    fun deleteGrocery(context: Context, grocery: GroceryItem) {
        val groceries = loadGroceries(context)
        groceries.remove(grocery)
        saveGroceries(context, groceries)
    }
}
