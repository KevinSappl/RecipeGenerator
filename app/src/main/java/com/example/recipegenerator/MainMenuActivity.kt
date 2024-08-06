@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.recipeDB.RecipeDB
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeGeneratorTheme {
                MainMenuScreen(
                    onFoodsClick = { startActivity(Intent(this, GroceriesOperationsActivity::class.java)) },
                    onScanClick = { startActivity(Intent(this, FoodScanner::class.java)) },
                    onFavouritesClick = { startActivity(Intent(this, Favourites::class.java)) },
                    onRecipeClick = { recipeId ->
                        val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                            putExtra("recipe_id", recipeId)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    onFoodsClick: () -> Unit,
    onScanClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onRecipeClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var recipes by remember { mutableStateOf(listOf<Recipe>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val loadedRecipes = withContext(Dispatchers.IO) {
                RecipeDB.getDatabase(context).recipeDao().getAll()
            }
            recipes = loadedRecipes
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Generator") },
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        items(recipes) { recipe ->
                            ListItem(recipe = recipe, onRecipeClick = { onRecipeClick(recipe.id) })
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onFoodsClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Lebensmittel", color = MaterialTheme.colorScheme.onPrimary, maxLines = 1)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        FloatingActionButton(
                            onClick = onScanClick,
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Text(text = "+", color = MaterialTheme.colorScheme.onPrimary, fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = onFavouritesClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Favoriten", color = MaterialTheme.colorScheme.onPrimary, maxLines = 1)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ListItem(recipe: Recipe, onRecipeClick: () -> Unit) {
    Text(
        text = recipe.name,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRecipeClick)
            .padding(16.dp)
    )
}
