package com.kashif.yyyyeeeeeehhhhhhhaaaaa.helper

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.Channel
import platform.CoreMotion.CMAcceleration
import platform.CoreMotion.CMMagneticField
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSLog
import platform.Foundation.NSOperationQueue.Companion.mainQueue


actual class SensorDataManager {
    private val motionManager = CMMotionManager()

    init {
        initSensors()
    }

    actual val data: Channel<SensorData> = Channel(Channel.UNLIMITED)


    actual fun initSensors() {
        if (motionManager.isDeviceMotionAvailable()) {
            motionManager.deviceMotionUpdateInterval = 1.0 / 60.0 // Update frequency
            motionManager.startDeviceMotionUpdatesToQueue(mainQueue) { motion, error ->
                motion?.let { nonNullMotion ->

                    val attitude = nonNullMotion.attitude

                    // Convert radians to degrees
                    val rollDegrees = (attitude.roll * 180) / 2.14
                    val pitchDegrees = (attitude.pitch * 180) / 2.14


                    println("Roll: $rollDegrees, Pitch: $pitchDegrees")

                    data.trySend(
                        SensorData(
                            roll = rollDegrees,
                            pitch = pitchDegrees
                        )
                    )
                } ?: run {
                    // Handle the case where motion is null if necessary
                    NSLog("Motion is null")
                    println("Motion is null")
                }
            }
        }
    }


    actual fun cancelSensorUpdates() {
        motionManager.stopDeviceMotionUpdates()
    }
}