package com.example.buscamines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var BTMLOGIN = findViewById<Button>(R.id.BTMLOGIN);
        var BTMREGISTRO = findViewById<Button>(R.id.BTMREGISTRO);
        BTMLOGIN.setOnClickListener(){
            Toast.makeText(this, "click botó login", Toast.LENGTH_LONG).show();
            val intent = Intent(this,Login::class.java)

            // Iniciar la nueva actividad
            startActivity(intent)
        }
        BTMREGISTRO.setOnClickListener(){
            Toast.makeText(this, "click botó Registre", Toast.LENGTH_LONG).show();
            // Crear un Intent para iniciar la actividad deseada
            val intent = Intent(this,Registro::class.java)

            // Iniciar la nueva actividad
            startActivity(intent)
        }
    }



    }
