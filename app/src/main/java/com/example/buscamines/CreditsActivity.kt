package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.fragment.app.Fragment

class CreditsActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var centerFragment: CenterFragment
    private lateinit var studentsFragment: StudentsFragment
    private lateinit var currentFragment: Fragment
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        centerFragment = CenterFragment()
        studentsFragment = StudentsFragment()
        currentFragment = centerFragment
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")

        // Iniciar la transacción del fragmento con el fragmento inicial
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, centerFragment) // Mostrar CenterFragment primero
            commit()
        }

        // Iniciar el temporizador para cambiar de fragmento después de 3 segundos
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, studentsFragment)
                addToBackStack(null)
                commit()
                currentFragment = studentsFragment
            }
        }, 3000)

        // Configurar el botón para regresar al menú principal
        buttonBack = findViewById(R.id.button_back)
        buttonBack.setTypeface(tf)
        buttonBack.setOnClickListener {
            val intent= Intent(this, Menu::class.java)
            startActivity(intent)
            finish() // Cierra esta actividad y regresa al menú principal
        }
    }

    override fun onStop() {
        super.onStop()
        // Detener el temporizador cuando la actividad está en segundo plano
        handler.removeCallbacksAndMessages(null)
    }

}
