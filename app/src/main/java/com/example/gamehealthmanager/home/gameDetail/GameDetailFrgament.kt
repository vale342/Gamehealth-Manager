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
import com.google.android.material.snackbar.Snackbar
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindGameInfo()
        setupListeners()
        observeViewModel()

        // Carga inicial
        viewModel.loadRating(game.id.toString()) { rating ->
            rating?.let { updateTrafficLightVisuals(it) }
        }

        binding.btnGreen.setOnClickListener { handleRatingClick("GREEN", it) }
        binding.btnYellow.setOnClickListener { handleRatingClick("YELLOW", it) }
        binding.btnRed.setOnClickListener { handleRatingClick("RED", it) }
    }

    private fun handleRatingClick(rating: String, view: View) {
        animateSelectedButton(view)
        updateTrafficLightVisuals(rating)

        viewModel.saveRating(game.id.toString(), game.titulo, rating) { success ->
            if (success) Snackbar.make(binding.root, "Diagnóstico guardado!", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun updateTrafficLightVisuals(selectedRating: String) {
        binding.containerGreen.setBackgroundResource(R.drawable.shape_circle_default)
        binding.containerYellow.setBackgroundResource(R.drawable.shape_circle_default)
        binding.containerRed.setBackgroundResource(R.drawable.shape_circle_default)

        val selectedContainer = when(selectedRating) {
            "GREEN" -> binding.containerGreen
            "YELLOW" -> binding.containerYellow
            else -> binding.containerRed
        }
        val colorRes = when(selectedRating) {
            "GREEN" -> R.color.green_status
            "YELLOW" -> R.color.yellow_status
            else -> R.color.red_status
        }
        selectedContainer.setBackgroundResource(R.drawable.shape_circle_filled)
        selectedContainer.background.setTint(requireContext().getColor(colorRes))
    }

    private fun animateSelectedButton(view: View) {
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100)
            .withEndAction { view.animate().scaleX(1.0f).scaleY(1.0f).duration = 100 }.start()
    }

    private fun bindGameInfo() {
        binding.tvTitle.text = game.titulo
        binding.tvGenre.text = "Genre: ${game.generos?.joinToString { it.name } ?: "No genre"}"
        binding.tvDescription.text = game.descripcion ?: "No description available."
        Glide.with(binding.ivCover).load(game.imagenUrl).centerCrop().into(binding.ivCover)
    }

    private fun setupListeners() {
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite(game) }
        binding.btnClose.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { isFav ->
                    binding.btnFavorite.setImageResource(if (isFav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}