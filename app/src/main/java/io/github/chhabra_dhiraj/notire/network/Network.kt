package io.github.chhabra_dhiraj.notire.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Network {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        redactHeader("Authorization")
        redactHeader("Cookie")
    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        readTimeout(300, TimeUnit.SECONDS)
        connectTimeout(300, TimeUnit.SECONDS)
        writeTimeout(300, TimeUnit.SECONDS)
        addInterceptor(httpLoggingInterceptor)
    }.build()

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.44:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notificationService: NotificationService? =
        retrofit.create(NotificationService::class.java)

}