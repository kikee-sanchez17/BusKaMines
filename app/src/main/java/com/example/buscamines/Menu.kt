package com.example.buscamines


import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class Menu : AppCompatActivity() {
    //reference serà el punter que ens envia a la base de dades de jugadors
    lateinit var reference: DatabaseReference
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth

    lateinit var imatgePerfil: ImageView

    /*butons*/
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var miperfilBtn: Button

    /*text menu*/
    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var uid: String
    var user:FirebaseUser? = null;
    lateinit var storageReference: StorageReference
    lateinit var folderReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        storageReference = FirebaseStorage.getInstance().getReference()
        folderReference = storageReference.child("FotosPerfil")
        /*botons*/
        tancarSessio = findViewById(R.id.tancarSessio)
        CreditsBtn = findViewById(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById(R.id.PuntuacionsBtn)
        jugarBtn = findViewById(R.id.jugarBtn)
        miperfilBtn=findViewById<Button>(R.id.miperfil)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        imatgePerfil=findViewById(R.id.imatgePerfil)
        consulta()
        tancarSessio.setOnClickListener() {
            tancalaSessio()
        }
        CreditsBtn.setOnClickListener() {
            val intent= Intent(this, CreditsActivity::class.java)
            startActivity(intent)
            finish()
        }

        miperfilBtn.setOnClickListener() {
            val intent= Intent(this, Perfil::class.java)
            intent.putExtra("UID_Jugador",uid)
            startActivity(intent)

        }
        PuntuacionsBtn.setOnClickListener() {

            val intent= Intent(this, Ranking::class.java)
            startActivity(intent)
            finish()

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
        miperfilBtn.setTypeface(tf)

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
                        // Referencia al objeto de almacenamiento de la imagen usando el UID del usuario como nombre del archivo
                        val imageReference = folderReference.child(uid)

                        imageReference.downloadUrl.addOnSuccessListener { uri ->
                            // URL de descarga obtenida con éxito
                            val url = uri.toString()
                            // Ahora puedes usar esta URL para cargar la imagen en tu ImageView usando Picasso u otra biblioteca
                            try {
                                Picasso.get().load(url).into(imatgePerfil)
                            } catch (e: Exception) {
                                Picasso.get().load(R.drawable.profile_pic).into(imatgePerfil)
                            }
                        }

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