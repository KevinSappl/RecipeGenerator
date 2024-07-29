package com.example.recipegenerator.recipeDB


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


// AI start (just sample Data, bc i didn't want to setup the AI

/*
val sampleRecipes = listOf(
    Recipe(1, "Spaghetti Carbonara", "Delicious pasta with eggs, cheese, and bacon", true),
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

 */


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
            /*
            (favouriteRecipes as MutableLiveData).postValue(sampleRecipes)      // sample Data

             */
            (favouriteRecipes as MutableLiveData).postValue(recipeDao.getFavourites())      // actual Data.


        }
    }
}