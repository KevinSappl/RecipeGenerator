package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class GroceriesOperationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groceries_operations)


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
            Log.d("RecipesOperationsActivity", "Loschen button clicked")
            val intent = Intent(this, GroceriesListActivity::class.java)
            startActivity(intent)
        }

    }
}
