package com.example.recipegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipegenerator.recipeDB.Recipe
import com.example.recipegenerator.recipegenerator.RecipeGenerator
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
    var braveMode by remember { mutableStateOf(false) }
    var generatedRecipe :Recipe? by remember { mutableStateOf(null) }

    //This tutorial helped me a bit: https://www.youtube.com/watch?v=FIEnIBq7Ups
    //And the code in "Favourites.kt"
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "recipegen") {
        composable("recipegen") {
            Scaffold(
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
                    BottomAppBar(
                        modifier = Modifier.background(PinkOrangeHorizontalGradient),
                        containerColor = Color.Transparent,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        ) {
                            Text(
                                color = Color.White,
                                text = "Brave mode:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Switch(
                                checked = braveMode,
                                onCheckedChange = { braveMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = Pink,
                                    checkedThumbColor = Orange
                                )
                            )


                            Text(
                                text = AnnotatedString(
                                    "This will force the AI to use all ingredients, " +
                                            "Even if they do not fit together. Use at your own risk.",
                                    spanStyles = listOf(
                                        AnnotatedString.Range(
                                            SpanStyle(fontWeight = FontWeight.Bold),
                                            81,
                                            101
                                        )
                                    )
                                ),
                                color = Color.White,
                                fontSize = 12.sp,
                            )

                        }
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(onClick =
                    {
                        generatedRecipe = RecipeGenerator.generateRecipe(groceries.toString(), braveMode)
                            navController.navigate("generatedRecipe")
                        
                    },
                        containerColor = Orange,
                        icon = {
                            Icon(
                                Icons.Rounded.SmartToy, tint = Color.White,
                                contentDescription = "",
                            )
                        },
                        text = {
                            Text(
                                "Generate",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        })
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(PinkOrangeHorizontalGradient, alpha = 0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(groceries) { grocery ->
                        GroceryItem(grocery)
                    }
                }

            }
        }
        
        composable("generatedRecipe"){
            RecipeDetails(recipe = generatedRecipe!!)
        }
    }
}

@Composable
fun GroceryItem(grocery: GroceryItem){
    Text(grocery.name, fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
        color = Color.White, fontWeight = FontWeight.Bold)
    HorizontalDivider()
}


@Composable
fun RecipeDetails(recipe: Recipe) {
    //Inside the "drawBehind" scope I cannot access colorscheme, that's why I need
    //to get the secondary color here.
    val secondaryColor = PinkOrangeHorizontalGradient;
    //This state variable is needed to get size of the border rectangles
    //I do not know if there is a better way of doing this.
    var borderVerticalWidth by remember { mutableStateOf(0f) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = recipe.name) },
            modifier = Modifier.background(PinkOrangeHorizontalGradient),
            backgroundColor = Color.Transparent,
            contentColor = Color.White
        )


    },
        modifier = Modifier
            .background(Color.White)
            .drawBehind { //Here I draw the lines on the sides and top+bottom of the background
                //Sadly I have not found an easier way to do this.
                drawRect(brush = secondaryColor, size = Size(size.width / 10f, size.height))
                drawRect(
                    brush = secondaryColor,
                    size = Size(borderVerticalWidth, size.height),
                    topLeft = Offset(size.width - borderVerticalWidth, 0f)
                )
                drawLine(
                    brush = secondaryColor,
                    start = Offset(0f, size.width / 20f),
                    end = Offset(size.width, size.width / 20f),
                    strokeWidth = size.width / 10f
                )
                drawLine(
                    brush = secondaryColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderVerticalWidth
                )
            }
            .padding(top = 20.dp)
            .onSizeChanged { size ->
                borderVerticalWidth = size.width / 10f;
            },
        backgroundColor = Color.Transparent)
    { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(
                    start = (borderVerticalWidth / LocalDensity.current.density).dp,
                    end = (borderVerticalWidth / LocalDensity.current.density).dp,
                    bottom = (borderVerticalWidth / 2 / LocalDensity.current.density).dp
                )
        ) {
            Text(text = recipe.details, modifier = Modifier.padding(horizontal = 5.dp))
        }

    }
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