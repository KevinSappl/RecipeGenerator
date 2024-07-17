package com.example.recipegenerator.http

import ApiResponse
import BarcodeResponseDataModel
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.recipegenerator.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class BarcodeHttpCoroutineWorker (appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        val barcode = inputData.getString("barcode") ?: return@coroutineScope Result.failure()
        val bearerToken = BuildConfig.BEARER_TOKEN //Must be declared in local.properties
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://ean-db.com/api/v2/product/$barcode")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return@coroutineScope try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                val gson = Gson()
                val type = object : com.google.gson.reflect.TypeToken<ApiResponse>() {}.type
                val apiResponse: ApiResponse = gson.fromJson(responseData, type)

                val title = apiResponse.product.titles.de
                val category = apiResponse.product.categories.firstOrNull()?.titles?.de ?: "Unknown"
                val weight = apiResponse.product.metadata?.generic?.weight?.firstOrNull()?.let {
                    "${it.value} ${it.unit}"
                } ?: "Unknown"

                val resultData = BarcodeResponseDataModel(title, category, weight)
                val outputData = Data.Builder()
                    .putString("title", resultData.title)
                    .putString("category", resultData.category)
                    .putString("weight", resultData.weight)
                    .build()

                Result.success(outputData)
            } else {
                Result.failure()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Result.retry()
        }
    }
}