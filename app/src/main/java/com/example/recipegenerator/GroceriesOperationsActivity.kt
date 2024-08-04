package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class GroceriesOperationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroceriesOperationsScreen(
                onAddGroceryClick = {
                    startActivity(Intent(this, FoodScanner::class.java))
                },
                onGenerateRecipeClick = {
                    startActivity(Intent(this, RecipeGenerationActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun GroceriesOperationsScreen(
    onAddGroceryClick: () -> Unit,
    onGenerateRecipeClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_64),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = MaterialTheme.typography.h4.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        onAddGroceryClick()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = stringResource(id = R.string.add_grocery_btn))
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        onGenerateRecipeClick()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = stringResource(id = R.string.gen_recipe_btn))
            }
        }
    }
}
