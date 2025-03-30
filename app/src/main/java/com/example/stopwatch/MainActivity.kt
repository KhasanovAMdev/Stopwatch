package com.example.stopwatch

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.SystemClock
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch: Chronometer
    private lateinit var ball: ImageView
    private var animator: ObjectAnimator? = null
    var running = false
    var offset: Long = 0
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ball = findViewById(R.id.ball)

        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                stopwatch.start()
                running = true
                startBallAnimation();
            }
            startButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale))
        }

        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                stopwatch.stop()
                running = false
            }
            stopBallAnimation();
            pauseButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale))
        }

        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
            resetButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale))
        }
    }


    //    override fun onStop() {
//        super.onStop()
    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    //    override fun onRestart() {
//        super.onRestart()
    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(outState)
    }

    fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base

    }


    private fun startBallAnimation() {
        animator = ObjectAnimator.ofFloat(ball, "rotation", 0f, 360f)
        animator?.duration = 1000
        animator?.repeatCount = ObjectAnimator.INFINITE
        animator?.start()
    }

    private fun stopBallAnimation() {
        animator?.cancel() // Останавливаем анимацию
        ball.rotation = 0f // Сбрасываем вращение шара, если необходимо
    }
}