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
    // reference will be the pointer that sends us to the players database
    lateinit var reference: DatabaseReference
    // create variables to check user and authentication
    lateinit var auth: FirebaseAuth

    lateinit var profileImage: ImageView

    /* buttons */
    lateinit var logoutButton: Button
    lateinit var creditsButton: Button
    lateinit var scoresButton: Button
    lateinit var playButton: Button
    lateinit var myProfileButton: Button

    /* menu text */
    lateinit var myScoreTxt: TextView
    lateinit var score: TextView
    lateinit var email: TextView
    lateinit var name: TextView
    lateinit var uid: String
    var user:FirebaseUser? = null;
    lateinit var storageReference: StorageReference
    lateinit var folderReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        storageReference = FirebaseStorage.getInstance().getReference()
        folderReference = storageReference.child("FotosPerfil")

        /* buttons */
        logoutButton = findViewById(R.id.tancarSessio)
        creditsButton = findViewById(R.id.CreditsBtn)
        scoresButton = findViewById(R.id.PuntuacionsBtn)
        playButton = findViewById(R.id.jugarBtn)
        myProfileButton=findViewById<Button>(R.id.miperfil)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        profileImage=findViewById(R.id.imatgePerfil)
        fetchData()
        logoutButton.setOnClickListener() {
            logoutUser()
        }
        creditsButton.setOnClickListener() {
            val intent= Intent(this, CreditsActivity::class.java)
            startActivity(intent)
            finish()
        }

        myProfileButton.setOnClickListener() {
            val intent= Intent(this, Perfil::class.java)
            intent.putExtra("UID_Player",uid)
            startActivity(intent)
        }
        scoresButton.setOnClickListener() {
            val intent= Intent(this, Ranking::class.java)
            startActivity(intent)
            finish()
        }
        playButton.setOnClickListener() {
            var names : String = name.getText().toString()
            var scores : String = score.getText().toString()

            val intent= Intent(this, SeleccioNivell::class.java)
            intent.putExtra("UID",uid)
            intent.putExtra("NAME",names)
            intent.putExtra("SCORE",scores)
            startActivity(intent)
            finish()
        }

        /* font */
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")

        /* menu text */
        myScoreTxt = findViewById(R.id.miPuntuaciotxt)
        score = findViewById(R.id.puntuacio)
        email = findViewById(R.id.correo)
        name = findViewById(R.id.nom)

        /* menu text */
        myScoreTxt.setTypeface(tf)
        score.setTypeface(tf)
        email.setTypeface(tf)
        name.setTypeface(tf)

        /* buttons */
        logoutButton.setTypeface(tf)
        creditsButton.setTypeface(tf)
        scoresButton.setTypeface(tf)
        playButton.setTypeface(tf)
        myProfileButton.setTypeface(tf)
    }

    override fun onStart() {
        checkLoggedInUser()
        super.onStart()
    }

    private fun logoutUser() {
        auth.signOut() // logout
        // go to the initial screen
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkLoggedInUser() {
        if (user !=null) {

        } else {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchData() {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://buscamines-11db7-default-rtdb.europe-west1.firebasedatabase.app/")
        var dbreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        dbreference.addValueEventListener (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var found: Boolean = false
                for (ds in snapshot.getChildren()) {
                    if (ds.child("Email").getValue().toString().equals(user?.email)){
                        found=true
                        score.setText(ds.child("Puntuacio").getValue().toString())
                        email.setText(ds.child("Email").getValue().toString())
                        name.setText(ds.child("Nom").getValue().toString())
                        uid=( ds.child("Uid").getValue().toString())
                        // Reference to the storage object of the image using the user's UID as the file name
                        val imageReference = folderReference.child(uid)

                        imageReference.downloadUrl.addOnSuccessListener { uri ->
                            // URL successfully obtained
                            val url = uri.toString()
                            // Now you can use this URL to load the image into your ImageView using Picasso or another library
                            try {
                                Picasso.get().load(url).into(profileImage)
                            } catch (e: Exception) {
                                Picasso.get().load(R.drawable.profile_pic).into(profileImage)
                            }
                        }

                    }

                    if (!found) {
                        Log.e ("ERROR","ERROR EMAIL NOT FOUND")
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e ("ERROR","ERROR DATABASE CANCEL")
            }
        })
    }
}
