package com.kashif.yyyyeeeeeehhhhhhhaaaaa.helper


import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.kashif.yyyyeeeeeehhhhhhhaaaaa.AndroidApp
import kotlinx.coroutines.channels.Channel

actual class SensorDataManager : SensorEventListener {

    private val sensorManager by lazy {
        AndroidApp.INSTANCE.getSystemService(SENSOR_SERVICE) as SensorManager
    }

    init {
        initSensors()
    }

    actual fun initSensors() {
        Log.d("SensorDataManager", "init")
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    actual val data: Channel<SensorData> = Channel(Channel.UNLIMITED)

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY)
            gravity = event.values

        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values

        if (gravity != null && geomagnetic != null) {
            val r = FloatArray(9)
            val i = FloatArray(9)

            if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)

                data.trySend(
                    SensorData(
                        roll = orientation[2] * 180 / Math.PI,
                        pitch = orientation[1] * 180 / Math.PI,
                    )
                )
            }
        }
    }

    actual fun cancelSensorUpdates() {
        Log.d("SensorDataManager", "cancel")
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

