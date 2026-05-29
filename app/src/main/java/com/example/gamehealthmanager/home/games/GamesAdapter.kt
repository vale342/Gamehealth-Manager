package com.example.gamehealthmanager.home.games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamehealthmanager.core.model.Game
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
            // Conectamos los datos del modelo Game con tus TextViews
            binding.tvTitle.text = game.titulo
            binding.tvGenre.text = game.genero

            // Cargamos la imagen usando Glide
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
            override fun areItemsTheSame(oldItem: Game, newItem: Game) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Game, newItem: Game) =
                oldItem == newItem
        }
    }
}