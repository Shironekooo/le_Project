package com.example.le_androidapp.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.le_androidapp.DeviceViewModel
import com.example.le_androidapp.data.DeviceReceiveManager
import com.example.le_androidapp.data.ble.DeviceBLEReceiveManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context):BluetoothAdapter{
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }

    @Provides
    @Singleton
    fun provideTempHumidityReceiveManager(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ):DeviceReceiveManager{
        return DeviceBLEReceiveManager(bluetoothAdapter,context)
    }

    @Provides
    @Singleton
    fun provideDeviceModel(
        deviceReceiveManager: DeviceReceiveManager
    ) : DeviceViewModel {
        return DeviceViewModel(deviceReceiveManager)
    }
}