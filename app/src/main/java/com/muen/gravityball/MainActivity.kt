package com.muen.gravityball

import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import java.util.Objects

class MainActivity : AppCompatActivity() {

    lateinit var sensorManager:SensorManager
    lateinit var gravity:Sensor

    lateinit var gameView: GameView
    var bx = 100f
    var by = 80f

    private val sensorListener:SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            //计算出重力在屏幕上的投影方向
            val values = event?.values!!
            //从加速度传感器xy轴读数折算小球运动速度x分量
            gameView.ball.dx = -(values[0]*gameView.ball.dLength)
            //从加速度传感器xy轴读数折算小球运动速度y分量
            gameView.ball.dy = (values[1]*gameView.ball.dLength)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //设置为竖屏模式
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //获得SensorManager对象
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onResume() {
        sensorManager.registerListener(sensorListener,gravity,SensorManager.SENSOR_DELAY_UI)
        gameView.viewDrawThread.pauseFlag = false
        gameView.ball.x = bx
        gameView.ball.y = by
        super.onResume()
    }

    override fun onPause() {
        sensorManager.unregisterListener(sensorListener)
        gameView.viewDrawThread.pauseFlag = true
        //记录小球的位置
        bx = gameView.ball.x
        by = gameView.ball.y
        super.onPause()
    }
}