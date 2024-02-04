package com.kashif.yyyyeeeeeehhhhhhhaaaaa.helper

import kotlinx.coroutines.channels.Channel


expect class SensorDataManager() {
    val data: Channel<SensorData>
    fun initSensors()
    fun cancelSensorUpdates()
}

data class SensorData(
    val roll: Double,
    val pitch: Double
)