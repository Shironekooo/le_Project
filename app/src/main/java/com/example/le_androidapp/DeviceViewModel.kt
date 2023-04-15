package com.example.le_androidapp

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.le_androidapp.data.ConnectionState
import com.example.le_androidapp.data.DeviceReceiveManager
import com.example.le_androidapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceReceiveManager: DeviceReceiveManager
) : ViewModel(){

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var pitch by mutableStateOf(0f)
        private set

    var roll by mutableStateOf(0f)
        private set

    var flex by mutableStateOf(0f)
        private set

    // var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private var _connectionState: MutableLiveData<ConnectionState> = MutableLiveData(ConnectionState.Uninitialized)

    val connectionState: LiveData<ConnectionState> = _connectionState

    private fun subscribeToChanges(){
        viewModelScope.launch {
            deviceReceiveManager.data.collect{ result ->
                when(result){
                    is Resource.Success -> {
                        _connectionState.value = result.data.connectionState
                        pitch = result.data.pitch
                        roll = result.data.roll
                        flex = result.data.flex
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        _connectionState.value = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        _connectionState.value = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun disconnect(){
        deviceReceiveManager.disconnect()
    }

    fun reconnect(){
        deviceReceiveManager.reconnect()
    }

    fun initializeConnection(){
        errorMessage = null
        subscribeToChanges()
        deviceReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        deviceReceiveManager.closeConnection()
    }

}