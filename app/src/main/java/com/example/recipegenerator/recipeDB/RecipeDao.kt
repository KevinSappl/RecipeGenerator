package com.example.recipegenerator.recipeDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query



@Dao
interface RecipeDao {
    @Query("SELECT *  FROM Recipe ")
    suspend fun getAll(): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE id = (:id)")
    suspend fun getById(id: Int) : Recipe

    @Query("SELECT * FROM Recipe WHERE name = (:name)")
    suspend fun getByName(name: String) : Recipe

    @Query("SELECT * FROM Recipe WHERE favourite = 1")
    suspend fun getFavourites() : List<Recipe>

    @Insert
    suspend fun insert(vararg recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("DELETE FROM Recipe")
    suspend fun deleteAll()
}