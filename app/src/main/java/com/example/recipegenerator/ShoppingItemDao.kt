package com.example.recipegenerator

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData


@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items")
    fun getAll(): LiveData<List<ShoppingItem>>

    @Insert
    suspend fun insert(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)
}
