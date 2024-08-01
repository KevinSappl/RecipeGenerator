package com.example.recipegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipegenerator.ui.theme.Orange
import com.example.recipegenerator.ui.theme.Pink
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme

class RecipeGenerationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeGeneratorTheme {
                RecipeGeneration(groceries = GroceryUtils.loadGroceries(context = this.applicationContext))
            }
        }
    }
}

@Composable
fun RecipeGeneration(groceries: MutableList<GroceryItem>) {

    Scaffold (
        modifier = Modifier.padding(top = 20.dp),
        topBar = {
            TopAppBar(
                title = { Text(text = "Generate Recipe") },
                modifier = Modifier.background(PinkOrangeHorizontalGradient),
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            )
        },
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.background(PinkOrangeHorizontalGradient),
                containerColor = Color.Transparent,
            ){
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                        Text(
                            color = Color.White,
                            text = "Brave mode:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = true,
                            onCheckedChange = {},
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Pink,
                                checkedThumbColor = Orange
                            )
                        )


                        Text(
                            text =  AnnotatedString("This will force the AI to use all ingredients, " +
                                    "Even if they do not fit together. Use at your own risk.",
                                spanStyles = listOf(
                                    AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold),81,101))),
                            color = Color.White,
                            fontSize = 12.sp,
                        )

                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { /*TODO*/ },
                containerColor = Orange,
                icon = { Icon(Icons.Rounded.SmartToy, tint = Color.White,
                    contentDescription = "", )},
                text = { Text(
                    "Generate",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )})
        }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(PinkOrangeHorizontalGradient, alpha = 0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(groceries){ grocery ->
                GroceryItem(grocery)
            }
        }

    }
}

@Composable
fun GroceryItem(grocery: GroceryItem){
    Text(grocery.name, fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
        color = Color.White, fontWeight = FontWeight.Bold)
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    RecipeGeneratorTheme {
        RecipeGeneration(mutableListOf(
            GroceryItem("Tomatoes"),
            GroceryItem("Potatoes"),
            GroceryItem("Carrots"),
            GroceryItem("Onions"),
            GroceryItem("Garlic")
        ))
    }
}