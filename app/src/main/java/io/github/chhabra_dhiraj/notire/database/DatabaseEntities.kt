package io.github.chhabra_dhiraj.notire.database

import android.provider.MediaStore
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.chhabra_dhiraj.notire.domain.Notification
import io.github.chhabra_dhiraj.notire.network.NotificationMongoSchema

@Entity
data class DatabaseNotification constructor(
    @PrimaryKey
    val alert_id: String,
    val date: String,
    val source: String,
    val title: String,
    val message: String,
    val links: String,
    val pinned: Boolean
)

fun List<DatabaseNotification>.asDomainModel(): List<Notification> {
    return map {
        Notification(
            alert_id = it.alert_id,
            date = it.date,
            title = it.title,
            source = it.source,
            message = it.message,
            links = it.links,
            pinned = it.pinned
        )
    }
}