package io.github.chhabra_dhiraj.notire.domain

import android.os.Parcelable
import io.github.chhabra_dhiraj.notire.database.DatabaseNotification
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification(
    val alert_id: String,
    val date: String,
    val source: String,
    val title: String,
    val message: String,
    val links: String,
    var pinned: Boolean
) : Parcelable

fun Notification.asDatabaseModel(): DatabaseNotification {
    return DatabaseNotification(
        alert_id = alert_id,
        date = date,
        title = title,
        source = source,
        message = message,
        links = links,
        pinned = pinned
    )
}