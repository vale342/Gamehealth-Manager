package com.example.gamehealthmanager.home.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamehealthmanager.R

class FavoritesAdapter(private val games: List<Map<String, Any>>) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.ivCover)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvGenre: TextView = view.findViewById(R.id.tvGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        holder.tvTitle.text = game["titulo"] as? String ?: "Sin título"
        holder.tvGenre.text = game["genero"] as? String ?: "Género desconocido"

        val imageUrl = game["imagen"] as? String

        // Le decimos a Glide qué hacer si la URL es nula o falla
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray) // Pone un recuadro gris mientras carga
            .error(android.R.color.darker_gray)       // Pone un recuadro gris si es null
            .into(holder.ivCover)
    }

    override fun getItemCount() = games.size
}