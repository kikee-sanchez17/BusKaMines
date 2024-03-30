package com.example.buscamines

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var BTMLOGIN = findViewById<Button>(R.id.BTMLOGIN);
        var BTMREGISTRO = findViewById<Button>(R.id.BTMREGISTRO);

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        BTMLOGIN.setOnClickListener(){
            val intent = Intent(this,Login::class.java)

            // Iniciar la nueva actividad
            startActivity(intent)
            finish()
        }
        BTMREGISTRO.setOnClickListener(){
            // Crear un Intent para iniciar la actividad deseada
            val intent = Intent(this,Registro::class.java)

            // Iniciar la nueva actividad
            startActivity(intent)
            finish()
        }

    }
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }
    private fun usuariLogejat() {
        if (user !=null)
        {
            val intent= Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }




}
