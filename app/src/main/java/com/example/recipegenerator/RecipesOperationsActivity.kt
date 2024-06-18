package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class RecipesOperationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_operations)


        val btnHinzufugen = findViewById<Button>(R.id.btnHinzufügen)
        val btnLoschen = findViewById<Button>(R.id.btnLoschen)

        // Set click listeners for the buttons
        btnHinzufugen.setOnClickListener {
            // zu QR Scanning
            //val intent = Intent(this, QrCodeScannerActivity::class.java)
            //startActivity(intent)
        }

        btnLoschen.setOnClickListener {
            // Handle the click for Löschen button
            //val intent = Intent(this, RecipeListActivity::class.java)
            //startActivity(intent)
        }
    }
}
