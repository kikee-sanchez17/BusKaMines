package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class SeleccioNivell : AppCompatActivity() {
    lateinit var nivelFacilButton: Button
    lateinit var nivelMedioButton: Button
    lateinit var nivelDificilButton: Button
    lateinit var nivelExtremoButton: Button
    lateinit var backBtn: Button
    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccio_nivell)
        //ara recuperarem els valors
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")


        var intent:Bundle? = getIntent().extras
        UID = intent?.get("UID").toString()
        NOM = intent?.get("NOM").toString()
        PUNTUACIO = intent?.get("PUNTUACIO").toString()

        nivelFacilButton = findViewById<Button>(R.id.facil_button)
         nivelMedioButton = findViewById<Button>(R.id.intermedi_button)
         nivelDificilButton = findViewById<Button>(R.id.dificil_button)
         nivelExtremoButton = findViewById<Button>(R.id.extrema_button)
        backBtn=findViewById<Button>(R.id.back_btn)
        nivelDificilButton.setTypeface(tf)
        nivelMedioButton.setTypeface(tf)
        nivelFacilButton.setTypeface(tf)
        nivelExtremoButton.setTypeface(tf)
        nivelFacilButton.setOnClickListener(){
            iniciarJuego("facil")

        }
        nivelMedioButton.setOnClickListener(){
            iniciarJuego("medio")
        }
        nivelDificilButton.setOnClickListener(){
            iniciarJuego("dificil")
        }
        nivelExtremoButton.setOnClickListener(){
            iniciarJuego("extremo")
        }
        backBtn.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun iniciarJuego(nivelSeleccionado: String) {
        val intent = Intent(this, joc::class.java)
        intent.putExtra(
            "nivel",
            nivelSeleccionado
        )
        intent.putExtra("UID",UID)
        intent.putExtra("NOM",NOM)
        intent.putExtra("PUNTUACIO",PUNTUACIO)
        // Envía el nivel seleccionado a la siguiente actividad
        startActivity(intent)
        finish()
    }

}