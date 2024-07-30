package com.example.recipegenerator.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Custom:

val Pink = Color(0xFFBB499B)
val Orange = Color(0xFFF89829)

val PinkOrangeHorizontalGradient = Brush.horizontalGradient(
    colors = listOf(Pink, Orange)
)

/*
 how to import Gradient:

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import com.example.recipegenerator.ui.theme.Pink
import com.example.recipegenerator.ui.theme.Orange
import com.example.recipegenerator.ui.theme.PinkOrangeHorizontalGradient

                    TopAppBar(
                        title = { Text("Text") },
                        backgroundColor = Color.Transparent,
                        contentColor = Color.White,
                        modifier = Modifier
                            .background(PinkOrangeHorizontalGradient)
                    )

 */
