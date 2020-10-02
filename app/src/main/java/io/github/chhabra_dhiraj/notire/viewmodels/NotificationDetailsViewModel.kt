package io.github.chhabra_dhiraj.notire.viewmodels

import android.app.Application
import androidx.lifecycle.*
import io.github.chhabra_dhiraj.notire.database.getDatabase
import io.github.chhabra_dhiraj.notire.domain.Notification
import io.github.chhabra_dhiraj.notire.repository.NotificationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NotificationDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val notificationsRepository = NotificationsRepository(database)

    private val _isPinSuccess = MutableLiveData<Boolean>()

    val isPinSuccess: LiveData<Boolean>
        get() = _isPinSuccess

    private val _isDeleteSuccess = MutableLiveData<Boolean>()

    val isDeleteSuccess: LiveData<Boolean>
        get() = _isDeleteSuccess

    fun pinNotification(notification: Notification) {
        viewModelScope.launch {
            val response = notificationsRepository.pinNotification(notification)
            _isPinSuccess.value = response
        }
    }

    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            val response = notificationsRepository.deleteNotification(notification)
            _isDeleteSuccess.value = response
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationDetailsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}