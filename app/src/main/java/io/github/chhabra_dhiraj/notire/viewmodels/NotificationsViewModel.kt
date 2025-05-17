package io.github.chhabra_dhiraj.notire.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
import io.github.chhabra_dhiraj.notire.database.getDatabase
import io.github.chhabra_dhiraj.notire.repository.NotificationsRepository
import kotlinx.coroutines.*

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val notificationsRepository = NotificationsRepository(database)

    private val _isSuccess = MutableLiveData<Boolean>()

    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    val notifications = notificationsRepository.notifications

    init {
        refreshNotifications(application)
    }

    fun refreshNotifications(application: Application) {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            viewModelScope.launch {
                val response = notificationsRepository.refreshNotifications()
                _isSuccess.value = response
            }
        } else {
            _isSuccess.value = false
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}