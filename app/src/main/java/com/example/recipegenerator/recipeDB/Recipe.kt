package com.example.recipegenerator.recipeDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipegenerator.GroceryItem

@Entity
data class Recipe(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name") var name: String,
    //Should contain cooking instructions, macros of the recipe,needed cooking utensils and tips
    @ColumnInfo(name = "details") var details: String,
    @ColumnInfo(name = "favourite") var favourite: Boolean
)
