package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        recipesListView = findViewById(R.id.recipesList)

        // aufladen Rezepten aus DB
        loadRecipes()
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

        //recipesListView.setOnItemClickListener()

        //hier beim klicken wird Activity mit dem ausgewälten Rezept aufgerufen (activity_recipe_details.xml)
    }
}