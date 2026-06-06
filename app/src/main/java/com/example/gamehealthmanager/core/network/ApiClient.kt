package com.example.gamehealthmanager.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.rawg.io/api/"
    private const val API_KEY = "d18c8d4f62a944d381d156d4e31b02a4"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging) // Para ver los logs en Logcat
        .addInterceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("key", API_KEY) // Agrega la API Key a toda petición
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        .connectTimeout(30, TimeUnit.SECONDS) // Fundamental para la red de la escuela
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val GameApi: GameAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameAPI::class.java)
    }
}