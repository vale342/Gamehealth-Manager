package com.example.gamehealthmanager.home.games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.core.model.HealthRating
import com.example.gamehealthmanager.databinding.ItemGameBinding

class GamesAdapter(
    private val onItemClick: (Game) -> Unit = {}
) : ListAdapter<Game, GamesAdapter.GameViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GameViewHolder(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.tvTitle.text = game.titulo
            binding.tvGenre.text = game.generos?.joinToString { it.name } ?: "No genre"

            val rating = game.healthRating ?: HealthRating.NONE

            // Modificación aquí: Verificamos si tiene o no semáforo
            if (rating == HealthRating.NONE) {
                // Si no tiene semáforo (pantalla Home), forzamos el fondo oscuro
                binding.root.setBackgroundColor(android.graphics.Color.parseColor("#1A1A1A"))
            } else {
                // Si sí tiene semáforo (pantalla Favoritos/Ratings), le ponemos su color
                val colorRes = when (rating) {
                    HealthRating.GREEN -> R.color.green_status
                    HealthRating.YELLOW -> R.color.yellow_status
                    HealthRating.RED -> R.color.red_status
                    else -> R.color.disabled
                }
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, colorRes))
            }

            Glide.with(binding.ivCover.context)
                .load(game.imagenUrl)
                .centerCrop()
                .into(binding.ivCover)

            binding.root.setOnClickListener {
                onItemClick(game)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Game, newItem: Game) = oldItem == newItem
        }
    }
}