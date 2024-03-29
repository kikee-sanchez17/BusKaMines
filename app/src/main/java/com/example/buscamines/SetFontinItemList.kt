package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buscamines.adapter.JugadorsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
