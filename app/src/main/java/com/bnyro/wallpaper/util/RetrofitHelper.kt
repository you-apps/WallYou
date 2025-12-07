package com.bnyro.wallpaper.util

import com.bnyro.wallpaper.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


object RetrofitHelper {
    private const val USER_AGENT_HEADER =
        "Mozilla/5.0 (X11; Linux x86_64; rv:143.0) Gecko/20100101 Firefox/143.0"

    val json by lazy {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = true
            encodeDefaults = true
        }
    }

    val okHttpClient by lazy {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }

        // add user agent to all requests
        builder.addInterceptor { interceptorChain ->
            val request = interceptorChain.request()
                .newBuilder()
                .addHeader("User-Agent", USER_AGENT_HEADER)
            interceptorChain.proceed(request.build())
        }

        builder.build()
    }

    inline fun <reified T> create(baseUrl: String): T {
        val mediaType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .build()
            .create(T::class.java)
    }
}
