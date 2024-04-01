package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {

    // Declare variables to be used
    lateinit var emailLogin: EditText
    lateinit var passLogin: EditText
    lateinit var loginBtn: Button
    lateinit var noAccount:Button
    lateinit var loginTXT : TextView
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize variables by finding the views from the layout
        emailLogin = findViewById<EditText>(R.id.correoLogin)
        passLogin = findViewById<EditText>(R.id.passLogin)
        loginBtn = findViewById<Button>(R.id.BtnLogin)
        noAccount = findViewById(R.id.noaccount)
        loginTXT=findViewById(R.id.textoLogin)
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")
        emailLogin.setTypeface(tf)
        passLogin.setTypeface(tf)
        loginBtn.setTypeface(tf)
        noAccount.setTypeface(tf)
        loginTXT.setTypeface(tf)

        // Set OnClickListener to the login button
        loginBtn.setOnClickListener(){
            // Before login, validate the data
            var email:String = emailLogin.getText().toString()
            var password:String = passLogin.getText().toString()

            // Email validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailLogin.setError("Invalid Email")
            }
            // Password validation
            else if (password.length < 6) {
                passLogin.setError("Password must be at least 6 characters long")
            }
            else {
                loginUser(email, password)
            }
        }
        noAccount.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Function to handle player login
    private fun loginUser(email: String, password: String) {
        // Sign in with email and password
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUI(user)
            }
        }
    }

    // Function to update UI after successful login
    fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }
}
