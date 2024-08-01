@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipegenerator.recipeDB.FavouriteViewModel
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme
import androidx.compose.runtime.livedata.observeAsState

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import com.example.recipegenerator.ui.theme.Pink
import com.example.recipegenerator.ui.theme.Orange
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


class Favourites : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeGeneratorTheme {
                RecipeListScreen()
            }
        }
    }
}


@Composable
fun RecipeListScreen(viewModel: FavouriteViewModel = viewModel()) {
    val navController = rememberNavController()
    val favouriteRecipes by viewModel.favouriteRecipes.observeAsState(emptyList())

    NavHost(navController = navController, startDestination = "recipeList") {
        composable("recipeList") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Favorite Recipes") },
                        backgroundColor = Color.Transparent,
                        contentColor = Color.White,
                        modifier = Modifier
                            .background(PinkOrangeHorizontalGradient)
                            .padding(top = 40.dp),


                    )
                },
                content = { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)

                    ) {


                        if (favouriteRecipes.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No favourite recipes yet. :(")
                            }


                        } else {
                            // AI help start
                            RecipeList(
                                recipes = favouriteRecipes,
                                onRecipeClick = { recipe ->
                                    navController.navigate("recipeDetails/${recipe.id}")
                                }
                            )
                            // AI end
                        }

                    }
                }
            )
        }


        composable("recipeDetails/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt()
            val recipe = favouriteRecipes.find { it.id == recipeId }
            if (recipe != null) {
                RecipeDetailsScreen(recipe = recipe)
            } else {
                Text("Recipe not found")
            }
        }
    }
}



@Composable
fun RecipeList(recipes: List<Recipe>, onRecipeClick: (Recipe) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp, top = 10.dp)
    ) {
        items(recipes) { recipe ->
            RecipeItem(recipe = recipe, onRecipeClick = onRecipeClick)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}


@Composable
fun RecipeItem(recipe: Recipe, onRecipeClick: (Recipe) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onRecipeClick(recipe) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween


    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = recipe.name)
        }
    }
}


// Details page:

@Composable
fun RecipeDetailsScreen(recipe: Recipe) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.name) },
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                modifier = Modifier
                    .background(PinkOrangeHorizontalGradient)
                    .padding(top = 40.dp),
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()) // Couldn't scroll down without this ._.
            ) {
                Column {
                    Text(text = " ${recipe.details}")
                }
            }
        }
    )
}