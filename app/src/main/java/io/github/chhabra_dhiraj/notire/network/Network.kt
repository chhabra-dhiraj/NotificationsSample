package io.github.chhabra_dhiraj.notire.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.44:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notificationService: NotificationService? =
        retrofit.create(NotificationService::class.java)

}