package com.example.recipegenerator.recipeDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDB : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao


    companion object {
        @Volatile
        private var INSTANCE: RecipeDB? = null

        fun getDatabase(context: Context): RecipeDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDB::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}