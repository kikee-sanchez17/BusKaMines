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
    private lateinit var correoEt: EditText
    private lateinit var passEt: EditText
    private lateinit var passRepeatEt:EditText
    private lateinit var nombreEt: EditText
    private lateinit var fechaTxt: TextView
    private lateinit var Registrar: Button
    lateinit var auth: FirebaseAuth //FIREBASE AUTENTIFICACIO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        auth = FirebaseAuth.getInstance()
        correoEt = findViewById(R.id.correoEt)
        passEt = findViewById(R.id.passEt)
        nombreEt = findViewById(R.id.nombreEt)
        fechaTxt = findViewById(R.id.fechatexto)
        Registrar = findViewById(R.id.Registrar)
        passRepeatEt=findViewById(R.id.passEt2)

        // Obtener la fecha actual
        val date = Calendar.getInstance().time

        // Formatear la fecha como una cadena
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(date)

        // Establecer la fecha en el TextView
        fechaTxt.text = dateString

        Registrar.setOnClickListener() {
            //Abans de fer el registre validem les dades

            var email: String = correoEt.getText().toString()
            var pass: String = passEt.getText().toString()
            var passRepeat: String = passRepeatEt.getText().toString()
            if (!passwordValidate(this, pass, passRepeat)) {
                return@setOnClickListener
            }
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoEt.setError("Invalid Mail")
            } else if (pass.length < 6) {
                passEt.setError("Password less than 6 chars")
            } else {
                RegistrarJugador(email, pass)
            }

        }
        /*font*/
        val tf = Typeface.createFromAsset(assets,"fonts/Fredoka-Medium.ttf")
        /*text menu*/
        correoEt.setTypeface(tf)
        passEt.setTypeface(tf)
        nombreEt.setTypeface(tf)
        fechaTxt.setTypeface(tf)
        /*botons*/
        Registrar.setTypeface(tf)
        passRepeatEt.setTypeface(tf)
    }
    fun RegistrarJugador(email:String, passw:String){
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
    fun updateUI(user: FirebaseUser?){
        //hi ha un interrogant perquè podria ser null
        if (user!=null)
        {
            var puntuacio: Int = 0
            var uidString: String = user.uid
            var correoString: String = correoEt.getText().toString()
            var passString: String = passEt.getText().toString()
            var nombreString: String = nombreEt.getText().toString()
            var fechaString: String= fechaTxt.getText().toString()
            //AQUI GUARDA EL CONTINGUT A LA BASE DE DADES
            var dadesJugador : HashMap<String,String> = HashMap<String, String>()
            dadesJugador.put ("Uid",uidString)
            dadesJugador.put ("Email",correoString)
            dadesJugador.put ("Password",passString)
            dadesJugador.put ("Nom",nombreString)
            dadesJugador.put ("Data",fechaString)
            dadesJugador.put ("Puntuacio","0")
            dadesJugador
                .put (
                    "Imatge",
                    "")
            // Creem un punter a la base de dades i li donem un nom
            var database: FirebaseDatabase =
                FirebaseDatabase.getInstance("https://buscamines-11db7-default-rtdb.europe-west1.firebasedatabase.app/")
            var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
            if(reference!=null) {
                //crea un fill amb els valors de dadesJugador
                reference.child(uidString).setValue(dadesJugador)
                val intent = Intent(this,Menu::class.java)
                startActivity(intent)

            }

            finish()
        }

    }
    fun passwordValidate(context: Context, pass1: String, pass2: String): Boolean {
        return if (pass1 == pass2) {
            true // Las contraseñas son iguales
        } else {
            // Las contraseñas no coinciden, mostrar diálogo de alerta
            mostrarAlerta(context)
            false
        }
    }

    fun mostrarAlerta(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("Las contraseñas no coinciden.")
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }

}