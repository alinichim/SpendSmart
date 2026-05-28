package com.example.spendsmart.data.remote

import com.example.spendsmart.data.prefs.SecurePreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class MissingApiKeyException : IOException("ExchangeRate API key is not configured")

class ApiKeyInterceptor @Inject constructor(
    private val prefs: SecurePreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val key = prefs.getApiKey().takeIf { it.isNotBlank() }
            ?: throw MissingApiKeyException()

        val original = chain.request()
        val newUrl = original.url.toString().replace(PLACEHOLDER, key)
        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val PLACEHOLDER = "__APIKEY__"
    }
}
