package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.recipeDB.RecipeDB
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailActivity : ComponentActivity() {
    private var currentRecipeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentRecipeId = intent.getIntExtra("recipe_id", -1)

        setContent {
            RecipeGeneratorTheme {
                RecipeDetailScreen(
                    recipeId = currentRecipeId,
                    onEditClick = {
                        val intent = Intent(this, EditRecipeActivity::class.java).apply {
                            putExtra("recipe_id", currentRecipeId)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(recipeId: Int, onEditClick: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var recipe by remember { mutableStateOf<Recipe?>(null) }

    LaunchedEffect(recipeId) {
        if (recipeId != -1) {
            coroutineScope.launch {
                val loadedRecipe = withContext(Dispatchers.IO) {
                    RecipeDB.getDatabase(context).recipeDao().getById(recipeId)
                }
                recipe = loadedRecipe
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Details") },
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                recipe?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = it.details,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = onEditClick) {
                        Text("Edit Recipe")
                    }
                } ?: run {
                    Text("Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}
