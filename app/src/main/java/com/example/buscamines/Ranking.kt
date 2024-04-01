package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
        loadPlayersFromFirebase()

        backBtn.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
    //If the Profile image is clicked it changes the activity to the picked player information
    override fun onProfileImageClick(uid: String) {
        val intent = Intent(this, Perfil::class.java)
        intent.putExtra("UID_RankingPlayer",uid)
        startActivity(intent)
        finish()
    }

    fun initRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerOne)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jugadorsAdapter = JugadorsAdapter(emptyList(),this)
        recyclerView.adapter = jugadorsAdapter
    }

    private fun loadPlayersFromFirebase() {
        bdreference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                jugadors = mutableListOf<Jugador>()
                for (snapshot in dataSnapshot.children) {
                    val name = snapshot.child("Nom").getValue(String::class.java)
                    val score = snapshot.child("Puntuacio").getValue(String::class.java)
                    val uid=snapshot.child("Uid").getValue(String::class.java)
                    name?.let { name ->
                        score?.let { score ->
                            uid?.let { uid ->
                                jugadors.add(Jugador(uid,name, score))
                            }
                        }
                    }
                }
                // Sort the players by the score
                jugadors.sortByDescending { it.puntuacio.toInt() }
                jugadorsAdapter.jugadors = jugadors
                jugadorsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
