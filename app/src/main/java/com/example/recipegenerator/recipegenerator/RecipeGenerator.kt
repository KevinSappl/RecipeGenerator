package com.example.recipegenerator.recipegenerator

import com.example.recipegenerator.GroceryItem
import com.example.recipegenerator.recipeDB.Recipe


object RecipeGenerator {
    private val llmAPI: Llama3API = Llama3API();

    private val generateRecipeRequest = "Please generate a recipe with the following ingredients: "

    private val freeToChooseIngredientsRequest = "\nIf you think that some of the ingredients do not go well together," +
            "you do not have to use them in the recipe. "

    private val forceToUseAllIngredientsRequest ="\nYou have to use all ingredients."

    private val formatRequest = ". Please format the recipe in the following way:\n" +
            "**Name of Recipe**\n" +
            "Ingredients\n" +
            "Needed Cooking Utensils\n" +
            "Instructions\n" +
            "Cooking Tips\n" +
            "Macros\n";

    fun generateRecipe(groceries: MutableList<GroceryItem>, useAllIngredients: Boolean): Recipe {
        val ingredients = groceriesToString(groceries)

        val recipeGenerationPrompt: String = generateRecipeGenerationPrompt(ingredients, useAllIngredients)

        return turnResponseIntoRecipe(llmAPI.getResponse(recipeGenerationPrompt));
    }

    private fun generateRecipeGenerationPrompt(ingredients: String, useAllIngredients: Boolean): String{
        return generateRecipeRequest +
                ingredients +
                formatRequest +
                if(useAllIngredients) forceToUseAllIngredientsRequest else freeToChooseIngredientsRequest

    }

    private fun turnResponseIntoRecipe(toRecipe: String): Recipe {
        val substring = toRecipe.substringAfter("**")
        val nameOfRecipe = substring.substringBefore("**");
        val detailsOfRecipe = substring.substringAfter("**")

        return Recipe(name = nameOfRecipe, details = detailsOfRecipe, favourite = false);
    }

    private fun groceriesToString(groceries: MutableList<GroceryItem>): String {
        val ingredients = StringBuilder()

        for (grocery in groceries) {
            ingredients.append(grocery.name)
            ingredients.append(", ")
        }


        return ingredients.substring(0, ingredients.length -2)
    }


}