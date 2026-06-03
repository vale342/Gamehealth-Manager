package com.example.gamehealthmanager.home.gameDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.databinding.FragmentGameDetailBinding
import kotlinx.coroutines.launch

class GameDetailFragment : Fragment() {
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GameDetailViewModel>()
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = requireArguments().getParcelable("game") ?: error("Game argument required")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindGameInfo()
        setupListeners()
        observeViewModel()
    }

    private fun bindGameInfo() {
        binding.tvTitle.text = game.titulo

        // CORRECCIÓN: Convertimos la lista de géneros en un texto separado por comas
        val listaGeneros = game.generos?.joinToString { it.name } ?: "No genre"
        binding.tvGenre.text = "Genre: $listaGeneros"

        // CORRECCIÓN: Si la descripción viene nula, ponemos un texto por defecto
        binding.tvDescription.text = game.descripcion ?: "No description available."

        Glide.with(binding.ivCover)
            .load(game.imagenUrl)
            .centerCrop()
            .into(binding.ivCover)
    }

    private fun setupListeners() {
        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite(game)
        }
        binding.btnClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { isFav ->
                    binding.btnFavorite.setImageResource(
                        if (isFav) android.R.drawable.btn_star_big_on
                        else android.R.drawable.btn_star_big_off
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}