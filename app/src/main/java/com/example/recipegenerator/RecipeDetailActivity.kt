package com.example.recipegenerator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recipegenerator.recipeDB.RecipeDB
import com.example.recipegenerator.recipeDB.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipeTitleView: TextView
    private lateinit var ingredientsView: TextView
    private lateinit var instructionsView: TextView
    private lateinit var btnEditRecipe: Button
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeTitleView = findViewById(R.id.recipeTitle)
        ingredientsView = findViewById(R.id.ingridients)
        instructionsView = findViewById(R.id.instructions)
        btnEditRecipe = findViewById(R.id.btnEditRecipe)

        val recipeId = intent.getIntExtra("recipe_id", -1)
        if (recipeId != -1) {
            loadRecipe(recipeId)
        }

        btnEditRecipe.setOnClickListener {
            //TODO: Edit recipe e.g. through pop-up window
        }
    }

    private fun loadRecipe(recipeId: Int) {
        coroutineScope.launch {
            val recipe = withContext(Dispatchers.IO) {
                RecipeDB.getDatabase(this@RecipeDetailActivity).recipeDao().getById(recipeId)
            }
            updateUI(recipe)
        }
    }

    private fun updateUI(recipe: Recipe?) {
        recipe?.let {
            recipeTitleView.text = it.name
            instructionsView.text = it.details
        }
    }
}
