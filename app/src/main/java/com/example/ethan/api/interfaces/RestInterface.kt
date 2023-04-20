package com.example.ethan.api.interfaces

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class RestInterface {
    var client = OkHttpClient()

    @Throws(IOException::class)
    fun get(url: String?): String? {
        val request = Request.Builder()
            .url(url.toString())
            .build()
        client.newCall(request)
            .execute().use {
                    response -> return response.body!!.string() }
    }
}