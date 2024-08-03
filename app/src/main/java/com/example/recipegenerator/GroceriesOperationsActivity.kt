package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*


class GroceriesOperationsActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groceries_operations)


        val btnHinzufugen = findViewById<Button>(R.id.btnHinzufügen)
        val btnGroceryOperation = findViewById<Button>(R.id.btnGroceryOperation)

        // Set click listeners for the buttons
        btnHinzufugen.setOnClickListener {
            coroutineScope.launch {

                withContext(Dispatchers.Default) {
                    val intent = Intent(this@GroceriesOperationsActivity, FoodScanner::class.java)
                    startActivity(intent)
                }
            }
        }

        btnGroceryOperation.setOnClickListener {
            coroutineScope.launch {

                withContext(Dispatchers.Default) {
                    Log.d("GroceriesOperationsActivity", "Loschen button clicked")
                    runOnUiThread {
                        Toast.makeText(this@GroceriesOperationsActivity, "Loschen button clicked", Toast.LENGTH_LONG).show()
                    }
                    val intent = Intent(this@GroceriesOperationsActivity, GroceriesListActivity::class.java)
                    startActivity(intent)
                }
            }
        }



    }
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
