package com.example.recipegenerator

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView
import android.widget.Toast
import java.io.File

class GroceriesListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var groceryAdapter: GroceryAdapter
    private lateinit var searchView: SearchView
    private lateinit var btnGenerateRecipe: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groceries_list)

        createSampleJsonFile()

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        btnGenerateRecipe = findViewById(R.id.btnGenerateRecipe)

        val groceries = GroceryUtils.loadGroceries(this)
        groceryAdapter = GroceryAdapter(groceries.toMutableList()) { grocery ->
            deleteGrocery(grocery)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = groceryAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                groceryAdapter.filter.filter(newText)
                return true
            }
        })

        btnGenerateRecipe.setOnClickListener {
            val selectedGroceries = groceryAdapter.getSelectedItems()
            generateRecipe(selectedGroceries)
        }
    }

    private fun deleteGrocery(grocery: GroceryItem) {
        GroceryUtils.deleteGrocery(this, grocery)
        val updatedGroceries = GroceryUtils.loadGroceries(this)
        groceryAdapter.updateData(updatedGroceries.toMutableList())
    }

    private fun generateRecipe(selectedGroceries: List<GroceryItem>) {
        // Implement your recipe generation logic here
        // For example, display a Toast message with the selected groceries
        Toast.makeText(this, "Selected groceries: $selectedGroceries", Toast.LENGTH_LONG).show()
    }

    private fun createSampleJsonFile() {
        val file = File(filesDir, "groceries.json")
        if (!file.exists()) {
            val initialGroceries = listOf(
                GroceryItem("Tomatoes"),
                GroceryItem("Potatoes"),
                GroceryItem("Carrots"),
                GroceryItem("Onions"),
                GroceryItem("Garlic")
            )
            GroceryUtils.saveGroceries(this, initialGroceries)
        }
    }
}
