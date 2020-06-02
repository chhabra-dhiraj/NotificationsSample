package io.github.chhabra_dhiraj.notire.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.chhabra_dhiraj.notire.database.NotificationsDatabase
import io.github.chhabra_dhiraj.notire.database.asDomainModel
import io.github.chhabra_dhiraj.notire.domain.Notification
import io.github.chhabra_dhiraj.notire.domain.asDatabaseModel
import io.github.chhabra_dhiraj.notire.network.Network
import io.github.chhabra_dhiraj.notire.network.NotificationMongoSchema
import io.github.chhabra_dhiraj.notire.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

class NotificationsRepository(private val database: NotificationsDatabase) {

    val notifications: LiveData<List<Notification>> =
        Transformations.map(database.notificationDao.getNotifications()) {
            it.asDomainModel()
        }

    suspend fun refreshNotifications(): Boolean {
        var isSuccess = false
        withContext(Dispatchers.IO) {
            isSuccess = try {
                val response = Network.notificationService!!.getNotifications()
                if (response != null) {
                    database.notificationDao.insertAll(*response.asDatabaseModel())
                    true
                } else {
                    false
                }
            } catch (he: HttpException) {
                false
            }
        }
        return isSuccess
    }

    suspend fun pinNotification(notification: Notification): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                database.notificationDao.updateNotification(notification.asDatabaseModel())
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun deleteNotification(notification: Notification): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                database.notificationDao.deleteNotification(notification.asDatabaseModel())
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}