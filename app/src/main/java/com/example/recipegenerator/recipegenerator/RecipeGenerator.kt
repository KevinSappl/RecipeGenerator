package com.example.recipegenerator.recipegenerator



class RecipeGenerator {
    private val llmAPI: Llama3API = Llama3API();

    private val generateRecipeRequestPart1 = "Please generate a recipe with the following ingredients: "

    private val generateRecipeRequestPart2 = ". If you think that some of the ingredients do not go well together," +
            "you do not have to use them in the recipe. "

    private val formatRequest = " Please format the recipe in the following way:\n" +
            "Ingredients\n" +
            "Needed cooking utensils\n" +
            "Instructions\n" +
            "Cooking Tips\n" +
            "Macros\n";

    fun generateRecipe(ingredients: String): String {
        val recipeGenerationPrompt: String = generateRecipeGenerationPrompt(ingredients)

        return llmAPI.getResponse(recipeGenerationPrompt);
    }

    private fun generateRecipeGenerationPrompt(ingredients: String): String{
        return generateRecipeRequestPart1 + ingredients + generateRecipeRequestPart2 + formatRequest
    }

}