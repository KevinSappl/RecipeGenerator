package com.example.recipegenerator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
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
import com.example.recipegenerator.recipeDB.RecipeDB
import com.example.recipegenerator.recipegenerator.RecipeGenerator
import com.example.recipegenerator.ui.theme.Orange
import com.example.recipegenerator.ui.theme.Pink
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient
import com.example.recipegenerator.ui.theme.RecipeGeneratorTheme
import kotlinx.coroutines.runBlocking

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
    val context = LocalContext.current;
    NavHost(navController = navController, startDestination = "recipegen") {
        composable("recipegen") {
            Scaffold(
                modifier = Modifier.padding(top = 20.dp),
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Generate Recipe",
                            color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.background(PinkOrangeHorizontalGradient),
                        backgroundColor = Color.Transparent
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
                                ),
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )


                            Text(
                                text = AnnotatedString(
                                    "This will force the AI to use all ingredients, " +
                                            "even if they do not fit together. Use at your own risk.",
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
                        if(groceries.size == 0){
                            Toast.makeText(context, "Please add some ingredients.", Toast.LENGTH_LONG).show()
                            return@ExtendedFloatingActionButton
                        }

                        generatedRecipe = RecipeGenerator.generateRecipe(groceries, braveMode)
                        runBlocking { RecipeDB.getDatabase(context).recipeDao().insert(
                            generatedRecipe!!
                        )};
                            navController.navigate("generatedRecipe")
                        
                    },
                        containerColor = Orange,
                        icon = {
                            Icon(
                                Icons.Rounded.SmartToy, tint = Color.White,
                                contentDescription = "Generate Recipe",
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
            val recipe = runBlocking { RecipeDB.getDatabase(context).recipeDao().getByName(generatedRecipe!!.name)};
            RecipeDetails(recipe = recipe )
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

    var isFavourite by remember { mutableStateOf(recipe.favourite) }

    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = recipe.name, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = Color.White) },
            modifier = Modifier.background(PinkOrangeHorizontalGradient),
            backgroundColor = Color.Transparent,
            actions = {
                IconButton(onClick = {
                    recipe.favourite = !recipe.favourite
                    isFavourite = recipe.favourite

                   runBlocking { RecipeDB.getDatabase(context).recipeDao().update(recipe)};
                }){
                    Icon(painter = painterResource(
                        id = if(isFavourite) R.drawable.star_filled_512 else R.drawable.star_512 ),
                        contentDescription = "Favourite Recipe",
                        Modifier
                            .padding(horizontal = 5.dp)
                            .size(30.dp),
                        tint = Color.White)
                }
            }

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
        containerColor = Color.Transparent)
    { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(
                    start = (borderVerticalWidth / LocalDensity.current.density).dp,
                    end = (borderVerticalWidth / LocalDensity.current.density).dp,
                    bottom = (borderVerticalWidth / 2 / LocalDensity.current.density).dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = recipe.details, modifier = Modifier.padding(horizontal = 5.dp),
                fontSize = 14.sp)
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
       /* RecipeDetails(recipe = Recipe(1, "Schnitzel",
            "SHSDIHJAOSIDJASOIDJIOASDJIOSAJD", false))*/
    }

}