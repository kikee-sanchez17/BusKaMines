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

class Ranking : AppCompatActivity(), JugadorsAdapter.OnProfileImageClickListener {
    var jugadors = mutableListOf<Jugador>()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://buscamines-11db7-default-rtdb.europe-west1.firebasedatabase.app/")
    var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
    private lateinit var jugadorsAdapter: JugadorsAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var backBtn: Button
    lateinit var rankingtv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")
        backBtn = findViewById<Button>(R.id.back_btn)
        rankingtv=findViewById<TextView>(R.id.ranking_tv)
        backBtn.setTypeface(tf)
        rankingtv.setTypeface(tf)
        initRecyclerView()
        loadJugadoresFromFirebase()

        backBtn.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onProfileImageClick(uid: String) {
        Toast.makeText(this, "UID del jugador: $uid", Toast.LENGTH_LONG).show()
        val intent = Intent(this, Perfil::class.java)
        intent.putExtra("UID_RankingPlayer",uid)
        startActivity(intent)
        finish()
        // Aquí puedes realizar cualquier acción adicional que necesites con el UID, por ejemplo, pasar a otra actividad
    }

    fun initRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerOne)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jugadorsAdapter = JugadorsAdapter(emptyList(),this)
        recyclerView.adapter = jugadorsAdapter
    }

    private fun loadJugadoresFromFirebase() {
        bdreference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                jugadors = mutableListOf<Jugador>()
                for (snapshot in dataSnapshot.children) {
                    val nombre = snapshot.child("Nom").getValue(String::class.java)
                    val puntuacion = snapshot.child("Puntuacio").getValue(String::class.java)
                    val uid=snapshot.child("Uid").getValue(String::class.java)
                    nombre?.let { nombre ->
                        puntuacion?.let { puntuacion ->
                            uid?.let { uid ->
                                jugadors.add(Jugador(uid,nombre, puntuacion))
                            }
                        }
                    }
                }
                // Ordenar la lista de jugadores por puntuación
                jugadors.sortByDescending { it.puntuacio.toInt() }
                jugadorsAdapter.jugadors = jugadors
                jugadorsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
