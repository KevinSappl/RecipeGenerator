package com.example.recipegenerator.recipegenerator

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception
import ru.gildor.coroutines.okhttp.await


//API is provided by https://console.groq.com/
//The model used is llama3 70b

class Llama3API {
    private val requestCreator: Llama3RequestCreator = Llama3RequestCreator("")
    private val client = OkHttpClient();

    fun getResponse (messageToLlama3: String): String {
        val request = requestCreator.buildRequest(messageToLlama3);

        val responseBody = sendRequest(request)

        return extractResponse(responseBody);
    }

    //Here I get the actual response from Llama3 to the message.
    private fun extractResponse(responseBody: JSONObject) : String{
        return (responseBody.getJSONArray("choices")[0] as JSONObject).getJSONObject("message").getString("content");
    }

    private fun sendRequest(request: Request): JSONObject {
        val response = runBlocking { client.newCall(request).await() }

        if(response.code != 200) throw Exception("Something went wrong!")

        val bodyString = response.body!!.string()

        response.close()

        return JSONObject(bodyString)
    }
}