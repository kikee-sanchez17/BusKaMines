package com.example.buscamines

import android.media.MediaPlayer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private val duracio: Long = 3000;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //amaguem la barra, pantalla a full
        supportActionBar?.hide()
        canviarActivity();
        // Inicializar el MediaPlayer con el sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_splash)

        // Comenzar la reproducción del sonido
        mediaPlayer?.start()

        // Manejar la finalización del sonido y pasar a la siguiente actividad
        mediaPlayer?.setOnCompletionListener {
            // Aquí puedes iniciar la siguiente actividad
            // Por ejemplo, puedes iniciar la actividad de login después de que el sonido termine
            // val intent = Intent(this, LoginActivity::class.java)
            // startActivity(intent)

            // Finalizar la actividad actual
            finish()
        }
    }

    private fun canviarActivity() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, duracio)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Liberar recursos de MediaPlayer
    }
}