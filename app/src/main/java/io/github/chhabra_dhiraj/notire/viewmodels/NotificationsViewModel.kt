package io.github.chhabra_dhiraj.notire.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.chhabra_dhiraj.notire.database.getDatabase
import io.github.chhabra_dhiraj.notire.repository.NotificationsRepository
import kotlinx.coroutines.launch

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val notificationsRepository = NotificationsRepository(database)
    val notifications = notificationsRepository.notifications

    init {
        refreshNotifications()
    }

    fun refreshNotifications() {
        viewModelScope.launch {
            notificationsRepository.refreshNotifications()
        }
    }
}