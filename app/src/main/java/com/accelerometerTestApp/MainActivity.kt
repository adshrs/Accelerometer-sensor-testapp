package com.accelerometerTestApp

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.accelerometerTestApp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

	private lateinit var binding: ActivityMainBinding
	private lateinit var sensorManager: SensorManager

	@RequiresApi(Build.VERSION_CODES.KITKAT)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

		setUpSensorStuff()
	}

	@RequiresApi(Build.VERSION_CODES.KITKAT)
	private fun setUpSensorStuff() {
		sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
			sensorManager.registerListener(
				this,
				it,
				SensorManager.SENSOR_DELAY_FASTEST,
				SensorManager.SENSOR_DELAY_FASTEST)
		}
	}

	override fun onSensorChanged(event: SensorEvent?) {
		if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
			val sides = event.values[0]
			val upDown = event.values[1]

			binding.tvSquare.apply {
				rotationX = upDown * 3f
				rotationY = sides * 3f
				rotation = -sides
				translationX = sides * -10
				translationY = upDown * 10
			}

			val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
			binding.tvSquare.setBackgroundColor(color)

			binding.tvSquare.text =  "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
		}
	}

	override fun onAccuracyChanged(event: Sensor?, p1: Int) {
		return
	}

	override fun onDestroy() {
		sensorManager.unregisterListener(this)
		super.onDestroy()
	}
}