package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
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

        var loginButton = findViewById<Button>(R.id.BTMLOGIN)
        var registerButton = findViewById<Button>(R.id.BTMREGISTRO)
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")
        loginButton.setTypeface(tf)
        registerButton.setTypeface(tf)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        loginButton.setOnClickListener(){
            val intent = Intent(this, Login::class.java)

            // Start the new activity
            startActivity(intent)
            finish()
        }
        registerButton.setOnClickListener(){
            // Create an Intent to start the desired activity
            val intent = Intent(this, Registro::class.java)

            // Start the new activity
            startActivity(intent)
            finish()
        }

    }
    override fun onStart() {
        checkLoggedInUser()
        super.onStart()
    }
    private fun checkLoggedInUser() {
        if (user !=null)
        {
            val intent= Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

}
