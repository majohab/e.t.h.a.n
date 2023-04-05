package com.example.ethan.api.interfaces

import okhttp3.*
import java.io.IOException


class RestInterface {
    var client = OkHttpClient()

    @Throws(IOException::class)
    fun get(url: String?): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).execute().use { response -> return response.body()!!.string() }
    }

    fun post(url: String?, headers: Headers, body: FormBody): String? {
        val request = Request.Builder().url(url).post(body).headers(headers).build()

        client.newCall(request).execute().use { response -> return response.body()!!.string() }
    }
}