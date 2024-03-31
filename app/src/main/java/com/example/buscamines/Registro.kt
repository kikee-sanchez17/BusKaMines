package com.example.buscamines

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Registro : AppCompatActivity() {
    private lateinit var emailEt: EditText
    private lateinit var passEt: EditText
    private lateinit var passRepeatEt: EditText
    private lateinit var nameEt: EditText
    private lateinit var dateTxt: TextView
    private lateinit var Register: Button
    lateinit var auth: FirebaseAuth // FIREBASE AUTHENTICATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        auth = FirebaseAuth.getInstance()
        emailEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nameEt = findViewById(R.id.nombreEt)
        dateTxt = findViewById(R.id.fechatexto)
        Register = findViewById(R.id.Registrar)
        passRepeatEt = findViewById(R.id.passEt2)

        // Get current date
        val date = Calendar.getInstance().time

        // Format the date as a string
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date)

        // Set the date in the TextView
        dateTxt.text = dateString

        Register.setOnClickListener() {
            // Before registration, validate data

            var email: String = emailEt.getText().toString()
            var pass: String = passEt.getText().toString()
            var passRepeat: String = passRepeatEt.getText().toString()
            if (!passwordValidate(this, pass, passRepeat)) {
                return@setOnClickListener
            }
            // Email validation
            // If not in email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEt.setError(getString(R.string.invalid_email))
            } else if (pass.length < 6) {
                passEt.setError(getString(R.string.short_password))
            } else {
                registerPlayer(email, pass)
            }

        }
        /*font*/
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")
        /*text menu*/
        emailEt.setTypeface(tf)
        passEt.setTypeface(tf)
        nameEt.setTypeface(tf)
        dateTxt.setTypeface(tf)
        /*buttons*/
        Register.setTypeface(tf)
        passRepeatEt.setTypeface(tf)
    }

    fun registerPlayer(email: String, passw: String) {
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                34
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                }
            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateUI(user: FirebaseUser?) {
        // There's a question mark because it could be null
        if (user != null) {
            var uidString: String = user.uid
            var emailString: String = emailEt.getText().toString()
            var passString: String = passEt.getText().toString()
            var nameString: String = nameEt.getText().toString()
            var dateString: String = dateTxt.getText().toString()
            // HERE SAVES THE CONTENT TO THE DATABASE
            var playerData: HashMap<String, String> = HashMap<String, String>()
            playerData.put("Uid", uidString)
            playerData.put("Email", emailString)
            playerData.put("Password", passString)
            playerData.put("Name", nameString)
            playerData.put("Date", dateString)
            playerData.put("Score", "0")

            // Create a pointer to the database and give it a name
            var database: FirebaseDatabase =
                FirebaseDatabase.getInstance("https://buscamines-11db7-default-rtdb.europe-west1.firebasedatabase.app/")
            var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
            if (reference != null) {
                // creates a child with the values of playerData
                reference.child(uidString).setValue(playerData)
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)

            }

            finish()
        }

    }

    fun passwordValidate(context: Context, pass1: String, pass2: String): Boolean {
        return if (pass1 == pass2) {
            true // Passwords match
        } else {
            // Passwords don't match, show alert dialog
            showAlert(context)
            false
        }
    }

    fun showAlert(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.error_title))
        builder.setMessage(context.getString(R.string.error_message))
        builder.setPositiveButton(context.getString(R.string.accept_button), null)
        val dialog = builder.create()
        dialog.show()
    }
}
