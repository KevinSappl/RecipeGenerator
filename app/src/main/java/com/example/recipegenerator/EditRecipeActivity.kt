package com.example.recipegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.recipeDB.RecipeDB
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRecipeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeId = intent.getIntExtra("recipe_id", -1)

        setContent {
            RecipeGeneratorTheme {
                EditRecipeScreen(
                    recipeId = recipeId,
                    onSaveClick = { updatedRecipe ->
                        saveRecipe(updatedRecipe)
                        finish()
                    }
                )
            }
        }
    }

    private fun saveRecipe(recipe: Recipe) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                RecipeDB.getDatabase(this@EditRecipeActivity).recipeDao().update(recipe)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(recipeId: Int, onSaveClick: (Recipe) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var recipe by remember { mutableStateOf<Recipe?>(null) }
    var recipeName by remember { mutableStateOf("") }
    var recipeDetails by remember { mutableStateOf("") }

    LaunchedEffect(recipeId) {
        if (recipeId != -1) {
            coroutineScope.launch {
                val loadedRecipe = withContext(Dispatchers.IO) {
                    RecipeDB.getDatabase(context).recipeDao().getById(recipeId)
                }
                recipe = loadedRecipe
                recipeName = loadedRecipe?.name ?: ""
                recipeDetails = loadedRecipe?.details ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Recipe") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .background(PinkOrangeHorizontalGradient)
                    .padding(top = 40.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                recipe?.let {
                    OutlinedTextField(
                        value = recipeName,
                        onValueChange = { recipeName = it },
                        label = { Text("Recipe Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = recipeDetails,
                        onValueChange = { recipeDetails = it },
                        label = { Text("Recipe Details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    Button(onClick = {
                        val updatedRecipe = recipe!!.copy(
                            name = recipeName,
                            details = recipeDetails
                        )
                        onSaveClick(updatedRecipe)
                    }) {
                        Text("Save Recipe")
                    }
                } ?: run {
                    Text("Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}
