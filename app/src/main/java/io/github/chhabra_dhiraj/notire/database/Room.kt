package io.github.chhabra_dhiraj.notire.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NotificationDao {
    @androidx.room.Query("select * from databasenotification")
    fun getNotifications(): LiveData<List<DatabaseNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg notifications: DatabaseNotification)

    @Update
    fun updateNotification(notification: DatabaseNotification?)

    @Delete
    fun deleteNotification(notification: DatabaseNotification?)
}

@Database(entities = [DatabaseNotification::class], version = 1, exportSchema = false)
abstract class NotificationsDatabase : RoomDatabase() {
    abstract val notificationDao: NotificationDao
}

private lateinit var INSTANCE: NotificationsDatabase

fun getDatabase(context: Context): NotificationsDatabase {
    synchronized(NotificationsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NotificationsDatabase::class.java,
                "notifications"
            ).build()
        }
    }
    return INSTANCE
}