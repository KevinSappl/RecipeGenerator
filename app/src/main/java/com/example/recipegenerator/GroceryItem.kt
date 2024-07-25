package com.example.recipegenerator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroceryItem(val name: String) : Parcelable

