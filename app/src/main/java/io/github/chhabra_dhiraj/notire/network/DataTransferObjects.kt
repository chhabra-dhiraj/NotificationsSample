package io.github.chhabra_dhiraj.notire.network

import io.github.chhabra_dhiraj.notire.database.DatabaseNotification
import io.github.chhabra_dhiraj.notire.domain.Notification

data class NotificationMongoSchema(
    val alert_id: String,
    val date: String,
    val source: String,
    val title: String,
    val message: String,
    val links: String
)

/**
 * Convert Network results to database objects
 */
fun List<NotificationMongoSchema>.asDomainModel(): List<Notification> {
    return map {
        Notification(
            alert_id = it.alert_id,
            date = it.date,
            title = it.title,
            source = it.source,
            message = it.message,
            links = it.links,
            pinned = false
        )
    }
}

fun List<NotificationMongoSchema>.asDatabaseModel(): Array<DatabaseNotification> {
    return map {
        DatabaseNotification(
            alert_id = it.alert_id,
            date = it.date,
            title = it.title,
            source = it.source,
            message = it.message,
            links = it.links,
            pinned = false
        )
    }.toTypedArray()
}