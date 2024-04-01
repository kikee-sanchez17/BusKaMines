package com.example.buscamines

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Bundle

class Splash : AppCompatActivity() {
    private val duration: Long = 3000;
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        // Initialize the sound with mediaplayer
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_splash)

        // Play the sound
        mediaPlayer.start()

        canviarActivity();
    }

    private fun canviarActivity() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            mediaPlayer.release()
        }, duration)
    }
}
