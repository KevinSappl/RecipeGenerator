package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.recipeDB.RecipeDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenuActivity : AppCompatActivity() {
    private lateinit var recipesListView: ListView
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var btnFoods: Button
    private lateinit var btnScan: Button
    private lateinit var btnFavourites: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        recipesListView = findViewById(R.id.recipesList)
        btnFoods = findViewById(R.id.btnFoods)
        btnScan = findViewById(R.id.btnScan)


        // aufladen Rezepten aus DB
        loadRecipes()

        btnFoods.setOnClickListener {
            val intent = Intent(this, GroceriesOperationsActivity::class.java)
            startActivity(intent)
        }

        btnScan.setOnClickListener{
            val intent = Intent(this, FoodScanner::class.java)
            startActivity(intent)
        }
        btnFavourites.setOnClickListener{
            //TODO:Favourites Activity
        }
    }

    // RezeptenList dynamisch auffüllen
    private fun loadRecipes() {
        coroutineScope.launch {
            val recipes = withContext(Dispatchers.IO) {
                RecipeDB.getDatabase(this@MainMenuActivity).recipeDao().getAll()
            }
            updateUI(recipes)
        }
    }

    private fun updateUI(recipes: List<Recipe>) {
        val recipeNames = recipes.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeNames)
        recipesListView.adapter = adapter

        recipesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedRecipe = recipes[position]
            val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                putExtra("recipe_id", selectedRecipe.id)
            }
            startActivity(intent)
        }
    }
}