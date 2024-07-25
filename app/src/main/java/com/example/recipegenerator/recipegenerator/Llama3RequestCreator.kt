package com.example.recipegenerator.recipegenerator

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class Llama3RequestCreator(val APIKey: String) {

    private val url = "https://api.groq.com/openai/v1/chat/completions";
    private val modelName = "llama3-70b-8192"
    private val authHeaderName = "Authorization"
    private val authHeaderValue = "Bearer " + APIKey;
    private val contentHeaderName = "Content-Type";
    private val contentHeaderValue = "application/json"


    fun buildRequest(messageToLlama3: String): Request {
        return Request.Builder()
            .url(url)
            .addHeader(authHeaderName,authHeaderValue)
            .addHeader(contentHeaderName, contentHeaderValue)
            .post(createRequestBody(messageToLlama3))
            .build()
    }

    private fun createRequestBody(messageToLlama3: String) : RequestBody {
        //https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json

        val mediaType = "application/json; charset=utf-8".toMediaType()
        return JSONObject("{\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"" + messageToLlama3 + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"model\": \""+ modelName + "\"\n" +
                "}").toString().toRequestBody(mediaType)
    }
}