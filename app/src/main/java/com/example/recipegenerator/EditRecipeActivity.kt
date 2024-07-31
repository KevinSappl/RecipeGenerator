package com.example.recipegenerator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.recipeDB.RecipeDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRecipeActivity : AppCompatActivity() {

    private lateinit var editRecipeName: EditText
    private lateinit var editDetails: EditText
    private lateinit var btnSave: Button
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        editRecipeName = findViewById(R.id.editRecipeName)
        editDetails = findViewById(R.id.editDetails)
        btnSave = findViewById(R.id.btnSave)

        val recipeId = intent.getIntExtra("recipe_id", -1)
        if (recipeId != -1) {
            loadRecipe(recipeId)
        }

        btnSave.setOnClickListener {
            saveRecipe()
        }
    }

    private fun loadRecipe(recipeId: Int) {
        coroutineScope.launch {
            val recipe = withContext(Dispatchers.IO) {
                RecipeDB.getDatabase(this@EditRecipeActivity).recipeDao().getById(recipeId)
            }
            currentRecipe = recipe
            updateUI(recipe)
        }
    }

    private fun updateUI(recipe: Recipe?) {
        recipe?.let {
            editRecipeName.setText(it.name)
            editDetails.setText(it.details)
        }
    }

    private fun saveRecipe() {
        val recipe = currentRecipe ?: return

        recipe.name = editRecipeName.text.toString()
        recipe.details = editDetails.text.toString()

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                RecipeDB.getDatabase(this@EditRecipeActivity).recipeDao().update(recipe)
            }
            finish()  // close after saving
        }
    }
}
