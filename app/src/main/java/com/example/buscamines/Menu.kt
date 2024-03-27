package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Menu : AppCompatActivity() {
    //reference serà el punter que ens envia a la base de dades de jugadors
    lateinit var reference: DatabaseReference
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    /*butons*/
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button

    /*text menu*/
    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var uid: String
    var user:FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        tancarSessio = findViewById<Button>(R.id.tancarSessio)
        CreditsBtn = findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn = findViewById<Button>(R.id.jugarBtn)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        tancarSessio.setOnClickListener() {
            tancalaSessio()
        }
        CreditsBtn.setOnClickListener() {
            Toast.makeText(this, "Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener() {
            Toast.makeText(this, "Puntuacions", Toast.LENGTH_SHORT).show()
        }
        jugarBtn.setOnClickListener() {

            var noms : String = nom.getText().toString()
            var puntuacios : String = puntuacio.getText().toString()

                val intent= Intent(this, SeleccioNivell::class.java)
                intent.putExtra("UID",uid)
            intent.putExtra("NOM",noms)
                intent.putExtra("PUNTUACIO",puntuacios)


            startActivity(intent)
                finish()
                    }

        /*font*/
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")

        /*text menu*/
        miPuntuaciotxt = findViewById(R.id.miPuntuaciotxt)
        puntuacio = findViewById(R.id.puntuacio)
        correo = findViewById(R.id.correo)
        nom = findViewById(R.id.nom)
        /*botons*/
        tancarSessio = findViewById(R.id.tancarSessio)
        CreditsBtn = findViewById(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById(R.id.PuntuacionsBtn)
        jugarBtn = findViewById(R.id.jugarBtn)

        /*text menu*/
        miPuntuaciotxt.setTypeface(tf)
        puntuacio.setTypeface(tf)
        correo.setTypeface(tf)
        nom.setTypeface(tf)
        /*botons*/
        tancarSessio.setTypeface(tf)
        CreditsBtn.setTypeface(tf)
        PuntuacionsBtn.setTypeface(tf)
        jugarBtn.setTypeface(tf)

        consulta()
    }
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun usuariLogejat()
    {
        if (user !=null)
        {
            Toast.makeText(this,"Jugador logejat",
                Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    //
    private fun consulta(){
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://buscamines-11db7-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DEBUG", "arrel value" + snapshot.getValue().toString())
                Log.i("DEBUG", "arrel key" + snapshot.key.toString())
                var trobat: Boolean = false
                for (ds in snapshot.getChildren()) {
                    Log.i ("DEBUG","DS key:"+ds.child("Uid").key.toString())
                    Log.i ("DEBUG","DS value:"+ds.child("Uid").getValue().toString())
                    Log.i ("DEBUG","DS data:"+ds.child("Data").getValue().toString())
                    Log.i ("DEBUG","DS mail:"+ds.child("Email").getValue().toString())

                    if (ds.child("Email").getValue().toString().equals(user?.email)){
                        trobat=true
                        puntuacio.setText(ds.child("Puntuacio").getValue().toString())
                        correo.setText(ds.child("Email").getValue().toString())
                        nom.setText(ds.child("Nom").getValue().toString())
                        uid=( ds.child("Uid").getValue().toString())

                    }

                    if (!trobat) {
                        Log.e ("ERROR","ERROR NO TROBAT MAIL")
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e ("ERROR","ERROR DATABASE CANCEL")
            }
        })
    }
    //


}