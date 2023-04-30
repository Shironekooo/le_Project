package com.example.le_androidapp.data;

sealed interface ConnectionState{
    data class Connected(val pitch: Float, val flex: Float): ConnectionState
    object Disconnected: ConnectionState
    object Uninitialized: ConnectionState
    object CurrentlyInitializing: ConnectionState
}