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
import androidx.navigation.fragment.findNavController

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

        viewModel.checkFavoriteStatus(game.id.toString())

        binding.btnGreen.setOnClickListener { handleRatingClick("GREEN", it) }
        binding.btnYellow.setOnClickListener { handleRatingClick("YELLOW", it) }
        binding.btnRed.setOnClickListener { handleRatingClick("RED", it) }
    }

    private fun handleRatingClick(rating: String, view: View) {
        animateSelectedButton(view)
        updateTrafficLightVisuals(rating)

        // Preparamos el texto del género igual que lo haces para la vista
        val generoText = game.generos?.joinToString { it.name } ?: "Género desconocido"

        // Ahora enviamos todos los parámetros que pide el ViewModel:
        // id, titulo, rating, imagenUrl, genero
        viewModel.saveRating(
            game.id.toString(),
            game.titulo,
            rating,
            game.imagenUrl,
            generoText
        ) { success ->
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
        // Si usas HTML, puedes usar esto:
        binding.tvDescription.text = android.text.Html.fromHtml(game.descripcion ?: "No description available.", android.text.Html.FROM_HTML_MODE_COMPACT)

        Glide.with(binding.ivCover).load(game.imagenUrl).centerCrop().into(binding.ivCover)
    }

    private fun setupListeners() {
        binding.btnFavorite.setOnClickListener {
            // Esto es todo lo que necesitas. Al ser un StateFlow,
            // el fragmento "escucha" el cambio y actualiza la estrella solo.
            viewModel.toggleFavorite(game.id.toString())
        }

        binding.btnClose.setOnClickListener {
            findNavController().navigateUp() // Es más limpio que llamar al dispatcher
        }
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