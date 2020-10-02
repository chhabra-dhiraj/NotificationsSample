package io.github.chhabra_dhiraj.notire.network

import retrofit2.http.GET

interface NotificationService {

    @GET("notifications")
    suspend fun getNotifications(): List<NotificationMongoSchema>?

}