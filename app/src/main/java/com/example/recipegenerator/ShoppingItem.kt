package com.example.recipegenerator

data class ShoppingItem(
    val id: Int,
    val name: String,
    val quantity: String = "",
    val details: String = ""
)
