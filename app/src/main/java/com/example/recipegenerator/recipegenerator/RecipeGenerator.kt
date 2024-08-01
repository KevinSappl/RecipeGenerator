package com.example.recipegenerator.recipegenerator



object RecipeGenerator {
    private val llmAPI: Llama3API = Llama3API();

    private val generateRecipeRequest = "Please generate a recipe with the following ingredients: "

    private val freeToChooseIngredientsRequest = "\nIf you think that some of the ingredients do not go well together," +
            "you do not have to use them in the recipe. "

    private val forceToUseAllIngredientsRequest ="\nYou have to use all ingredients."

    private val formatRequest = ". Please format the recipe in the following way:\n" +
            "Ingredients\n" +
            "Needed cooking utensils\n" +
            "Instructions\n" +
            "Cooking Tips\n" +
            "Macros\n";

    fun generateRecipe(ingredients: String, useAllIngredients: Boolean): String {
        val recipeGenerationPrompt: String = generateRecipeGenerationPrompt(ingredients, useAllIngredients)

        return llmAPI.getResponse(recipeGenerationPrompt);
    }

    private fun generateRecipeGenerationPrompt(ingredients: String, useAllIngredients: Boolean): String{
        return generateRecipeRequest +
                ingredients +
                formatRequest +
                if(useAllIngredients) forceToUseAllIngredientsRequest else freeToChooseIngredientsRequest

    }

}