package com.example.buscamines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buscamines.Jugador
import com.example.buscamines.R

class JugadorsAdapter(var jugadors: List<Jugador>, private val profileImageClickListener: OnProfileImageClickListener) : RecyclerView.Adapter<JugadorsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorsViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return JugadorsViewHolder(layoutInflater.inflate(R.layout.item_jugador, parent, false), profileImageClickListener)
    }


    override fun getItemCount(): Int {
        return jugadors.size
    }

    override fun onBindViewHolder(holder: JugadorsViewHolder, position: Int) {
        //aquest mètode és que va passant per cada un dels items i crida al render
        val item=jugadors[position]
        holder.render(item)
        holder.profilepic.setOnClickListener {
            profileImageClickListener.onProfileImageClick(item.uid)
        }
    }
    interface OnProfileImageClickListener {
        fun onProfileImageClick(uid: String)
    }
}
