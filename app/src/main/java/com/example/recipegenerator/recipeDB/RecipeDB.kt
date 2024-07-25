package com.example.recipegenerator.recipeDB

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDB : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}