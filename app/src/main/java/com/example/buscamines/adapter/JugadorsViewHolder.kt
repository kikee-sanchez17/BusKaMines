package com.example.buscamines.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buscamines.Jugador
import com.example.buscamines.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
class JugadorsViewHolder(view: View, private val profileImageClickListener: JugadorsAdapter.OnProfileImageClickListener) : RecyclerView.ViewHolder(view) {
    val namePlayer = view.findViewById<TextView>(R.id.tvNom_Jugador)
    val scoresPlayer = view.findViewById<TextView>(R.id.tvPuntuacio_Jugador)
    val profilepic = view.findViewById<ImageView>(R.id.ivJugador)
    lateinit var storageReference: StorageReference
    lateinit var folderReference: StorageReference

    lateinit var uid: String
    init {
        profilepic.setOnClickListener {
            profileImageClickListener.onProfileImageClick(uid)
        }
    }
    fun render(jugadorModel: Jugador) {

        namePlayer.text = jugadorModel.nom_jugador
        scoresPlayer.text = jugadorModel.puntuacio
        loadImagesUser(jugadorModel.uid)
    }

    private fun loadImagesUser(uid: String) {
        storageReference = FirebaseStorage.getInstance().getReference()
        folderReference = storageReference.child("FotosPerfil")
        val imageReference = folderReference.child(uid)

        imageReference.downloadUrl.addOnSuccessListener { uri ->
            // URL de descarga obtenida con éxito
            val url = uri.toString()
            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(url).resize(150, 150).into(profilepic)
        }.addOnFailureListener { exception ->
            Log.e("JugadorsViewHolder", "Error al cargar imagen: ${exception.message}")
            // Si ocurre un error al cargar la imagen, puedes mostrar una imagen de perfil predeterminada
            Picasso.get().load(R.drawable.profile_pic).resize(150, 150).into(profilepic)
        }
    }
}
