package com.example.recipegenerator.recipeDB


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


// AI start (just sample Data, bc i didn't want to setup the AI


val sampleRecipes = listOf(
    Recipe(1, "Scrambled Eggs", "Sure! Here's a simple and delicious recipe for scrambled eggs:\n" +
            "\n" +
            "Ingredients:\n" +
            "4 large eggs\n" +
            "1/4 cup milk (optional, for creaminess)\n" +
            "Salt and pepper to taste\n" +
            "1 tablespoon butter or oil\n" +
            "Optional: chopped herbs (like chives or parsley), shredded cheese, or diced vegetables\n" +
            "Instructions:\n" +
            "Crack the Eggs:\n" +
            "Crack the eggs into a medium-sized bowl. If you prefer creamier scrambled eggs, add the milk. This step is optional.\n" +
            "\n" +
            "Beat the Eggs:\n" +
            "Use a whisk or fork to beat the eggs until the yolks and whites are fully combined. Add a pinch of salt and a dash of pepper.\n" +
            "\n" +
            "Heat the Pan:\n" +
            "Place a non-stick skillet over medium-low heat and add the butter or oil. Allow it to melt and coat the bottom of the pan evenly.\n" +
            "\n" +
            "Cook the Eggs:\n" +
            "Pour the beaten eggs into the skillet. Let them sit for a few seconds without stirring to start setting.\n" +
            "\n" +
            "Stir and Scramble:\n" +
            "Using a spatula, gently stir the eggs, pushing them from the edges towards the center. Continue to cook, stirring occasionally, until the eggs are mostly set but still a bit runny.\n" +
            "\n" +
            "Finish Cooking:\n" +
            "Remove the skillet from the heat just before the eggs are fully cooked. The residual heat will finish cooking them, ensuring they stay soft and moist.\n" +
            "\n" +
            "Add Extras:\n" +
            "If you're adding cheese, herbs, or vegetables, fold them into the eggs now. Stir gently to combine.\n" +
            "\n" +
            "Serve:\n" +
            "Transfer the scrambled eggs to a plate and serve immediately. Enjoy!\n" +
            "\n" +
            "Feel free to customize this basic recipe with your favorite add-ins or toppings!", true),
    Recipe(2, "Chicken Alfredo", "Creamy chicken pasta with Alfredo sauce", true),
    Recipe(3, "Vegetable Stir-Fry", "Healthy and colorful stir-fry with various vegetables", false) ,
    Recipe(4, "Spaghetti Carbonara", "Delicious pasta with eggs, cheese, and bacon", true),
Recipe(5, "Chicken Alfredo", "Creamy chicken pasta with Alfredo sauce", true),
Recipe(6, "Vegetable Stir-Fry", "Healthy and colorful stir-fry with various vegetables", false),
Recipe(7, "Spaghetti Carbonara", "Delicious pasta with eggs, cheese, and bacon", true),
Recipe(8, "Chicken Alfredo", "Creamy chicken pasta with Alfredo sauce", true),
Recipe(9, "Vegetable Stir-Fry", "Healthy and colorful stir-fry with various vegetables", false),
Recipe(10, "Spaghetti Carbonara", "Delicious pasta with eggs, cheese, and bacon", true),
Recipe(11, "Chicken Alfredo", "Creamy chicken pasta with Alfredo sauce", true),
Recipe(12, "Vegetable Stir-Fry", "Healthy and colorful stir-fry with various vegetables", false),
Recipe(13, "Spaghetti Carbonara", "Delicious pasta with eggs, cheese, and bacon", true),
Recipe(14, "Chicken Alfredo", "Creamy chicken pasta with Alfredo sauce", true),
Recipe(15, "Vegetable Stir-Fry", "Healthy and colorful stir-fry with various vegetables", false),

)
// AI end




class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeDao: RecipeDao
    val favouriteRecipes: LiveData<List<Recipe>>

    init {
        val database = RecipeDB.getDatabase(application)
        recipeDao = database.recipeDao()
        favouriteRecipes = MutableLiveData()
        loadFavouriteRecipes()
    }

    private fun loadFavouriteRecipes() {
        viewModelScope.launch {

            (favouriteRecipes as MutableLiveData).postValue(sampleRecipes)      // sample Data
            /*

            (favouriteRecipes as MutableLiveData).postValue(recipeDao.getFavourites())      // actual Data.
*/

        }
    }
}