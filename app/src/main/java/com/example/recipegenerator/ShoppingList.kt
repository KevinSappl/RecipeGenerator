package com.example.recipegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme
import android.annotation.SuppressLint

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.graphics.Brush
import com.example.recipegenerator.ui.theme.Pink
import com.example.recipegenerator.ui.theme.Orange
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient



class ShoppingList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeGeneratorTheme {
                ShoppingListApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShoppingListApp(viewModel: ShoppingListViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }

    val shoppingItems by viewModel.shoppingItems.observeAsState(emptyList())

    val filteredItems = remember(shoppingItems, searchQuery) {          // item filtering in Search
        derivedStateOf {
            shoppingItems.filter {
                it.name.contains(searchQuery.text, ignoreCase = true)
            }
        }
    }.value

    if (showDialog) {       // add item to List
        AddItemDialog(
            onDismiss = { showDialog = false },
            onAddItem = { name, quantity ->
                viewModel.addItem(name, quantity)
                showDialog = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.padding(top = 40.dp),
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                modifier = Modifier.background(PinkOrangeHorizontalGradient),
                actions = {
                    TextField( // Search Field
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                            .background(PinkOrangeHorizontalGradient)

                    )
                }
            )
        },
        floatingActionButton = {        // Plus button

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Color(0xFFF89829),
                    contentColor = Color.White,



                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            }
        },
        content = {
            if (filteredItems.isEmpty()) {      // If no items available, show text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "This is your new Shopping List.")
                        Text(text = "Add some items to show here!")
                    }
                }
            } else {
                ShoppingList(                   // items zeigen.
                    items = filteredItems,
                    onItemClick = { /* Navigate to details screen. Probably wont implement that. */ },
                    onItemDelete = { item ->
                        viewModel.removeItem(item)
                    }
                )
            }
        }
    )
}

@Composable
fun AddItemDialog(onDismiss: () -> Unit, onAddItem: (String, String) -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add new item") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    label = { Text("Quantity") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAddItem(itemName, itemQuantity)
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Orange)

            ) {
                Text("Add", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

@Composable
fun ShoppingList(
    items: List<ShoppingItem>,
    onItemClick: (ShoppingItem) -> Unit,
    onItemDelete: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp, top = 50.dp)
    ) {
        items(items) { item ->
            ShoppingListItem(item = item, onItemClick = onItemClick, onItemDelete = onItemDelete)
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onItemClick: (ShoppingItem) -> Unit,
    onItemDelete: (ShoppingItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name)
            if (item.quantity.isNotEmpty()) {
                Text(text = "Quantity: ${item.quantity}", style = MaterialTheme.typography.body2)
            }
        }
        IconButton(onClick = { onItemDelete(item) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = Color.Red)
        }
    }
}