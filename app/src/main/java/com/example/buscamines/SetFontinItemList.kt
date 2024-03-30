package com.example.buscamines

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class SetFontinItemList : AppCompatActivity() {

    lateinit var nom: TextView
    lateinit var puntuacio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_jugador)
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")
        nom = findViewById<TextView>(R.id.tvNom_Jugador)
        puntuacio = findViewById<TextView>(R.id.tvPuntuacio_Jugador)
        nom.setTypeface(tf)
        puntuacio.setTypeface(tf)

    }
}
