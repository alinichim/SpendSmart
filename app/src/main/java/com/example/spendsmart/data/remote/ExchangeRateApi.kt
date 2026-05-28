package com.example.spendsmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    /**
     * ExchangeRate-API v6 endpoint.
     * The API key is injected as the first path segment by [ApiKeyInterceptor],
     * so callers leave it as the literal placeholder `__APIKEY__`.
     */
    @GET("v6/__APIKEY__/latest/{base}")
    suspend fun latest(@Path("base") base: String): ExchangeRateResponseDto
}
