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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FieldValue

class GameDetailFragment : Fragment() {
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GameDetailViewModel>()
    private lateinit var game: Game

    private val db = FirebaseFirestore.getInstance()

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

        // Aquí solo dejas las llamadas al click
        binding.btnGreen.setOnClickListener {
            animateSelectedButton(it)
            updateTrafficLightVisuals("GREEN")
            saveRating("GREEN")
        }
        binding.btnYellow.setOnClickListener {
            animateSelectedButton(it)
            updateTrafficLightVisuals("YELLOW")
            saveRating("YELLOW")
        }
        binding.btnRed.setOnClickListener {
            animateSelectedButton(it)
            updateTrafficLightVisuals("RED")
            saveRating("RED")
        }
    } // <--- Cierra onViewCreated aquí

    // La función va AFUERA, al mismo nivel que onViewCreated
    private fun saveRating(rating: String) {
        val gameId = game.id.toString()
        //val userId = "ID_DEL_USUARIO_ACTUAL"
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "usuario_anonimo"

        // IMPORTANTE: FieldValue necesita un import:
        // import com.google.firebase.firestore.FieldValue
        val ratingData = hashMapOf(
            "rating" to rating,
            "gameName" to game.titulo,
            "timestamp" to FieldValue.serverTimestamp() // Mucho mejor así
        )

        db.collection("users").document(userId).collection("ratings")
            .document(gameId)
            .set(ratingData)
            .addOnSuccessListener {
                // Asegúrate de tener el import de Snackbar
                Snackbar.make(binding.root, "Diagnóstico guardado!", Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun updateTrafficLightVisuals(selectedRating: String) {
        // 1. Resetear todos a default
        binding.containerGreen.setBackgroundResource(R.drawable.shape_circle_default)
        binding.containerYellow.setBackgroundResource(R.drawable.shape_circle_default)
        binding.containerRed.setBackgroundResource(R.drawable.shape_circle_default)

        // 2. Identificar el contenedor a llenar
        val selectedContainer = when(selectedRating) {
            "GREEN" -> binding.containerGreen
            "YELLOW" -> binding.containerYellow
            else -> binding.containerRed
        }

        // 3. Aplicar el color de relleno
        val colorRes = when(selectedRating) {
            "GREEN" -> R.color.green_status
            "YELLOW" -> R.color.yellow_status
            else -> R.color.red_status
        }

        selectedContainer.setBackgroundResource(R.drawable.shape_circle_filled)
        selectedContainer.background.setTint(requireContext().getColor(colorRes))
    }

    // Asegúrate de tener esto en tu clase
    private fun animateSelectedButton(view: View) {
        view.animate()
            .scaleX(1.2f).scaleY(1.2f)
            .setDuration(100)
            .withEndAction { view.animate().scaleX(1.0f).scaleY(1.0f).duration = 100 }
            .start()
    }

    private fun selectRating(rating: String, container: View) {
        // 1. Limpiamos todos los fondos (los ponemos en color neutro)
        binding.containerGreen.setBackgroundResource(R.drawable.shape_circle_default)
        binding.containerYellow.setBackgroundResource(R.drawable.shape_circle_default)
        binding.containerRed.setBackgroundResource(R.drawable.shape_circle_default)

        // 2. Pintamos solo el seleccionado
        val color = when(rating) {
            "GREEN" -> R.color.green_status
            "YELLOW" -> R.color.yellow_status
            else -> R.color.red_status
        }

        // Aplicamos el color al fondo del contenedor seleccionado
        container.setBackgroundResource(R.drawable.shape_circle_filled)
        container.background.setTint(requireContext().getColor(color))

        // 3. Animación
        animateSelectedButton(container)
        saveRating(rating)
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