package com.example.gamehealthmanager.home.gameLists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.core.model.Genre
import com.example.gamehealthmanager.core.model.HealthRating
import com.example.gamehealthmanager.databinding.FragmentGameListsBinding
import com.example.gamehealthmanager.home.games.GamesAdapter // <-- Usamos el adaptador bueno
import kotlinx.coroutines.launch

class GameListFragment : Fragment(R.layout.fragment_game_lists) {

    private var _binding: FragmentGameListsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameListsViewModel by viewModels()

    // 1. Preparamos los adaptadores con la instrucción de navegar al detalle al hacer clic
    private val gamesAdapter = GamesAdapter { game ->
        if (game.healthRating == null) game.healthRating = HealthRating.NONE
        val bundle = Bundle().apply { putParcelable("game", game) }
        findNavController().navigate(R.id.action_gameListsFragment_to_gameDetailFragment, bundle)
    }

    private val favoritesAdapter = GamesAdapter { game ->
        if (game.healthRating == null) game.healthRating = HealthRating.NONE
        val bundle = Bundle().apply { putParcelable("game", game) }
        findNavController().navigate(R.id.action_gameListsFragment_to_gameDetailFragment, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameListsBinding.bind(view)

        setupRecyclerViews()

        // 2. Asignamos los adaptadores a las listas
        binding.rvGames.adapter = gamesAdapter
        binding.rvFavorites.adapter = favoritesAdapter

        viewModel.loadAllData()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {

                // Observar Favoritos
                launch {
                    viewModel.favoriteGames.collect { firebaseList ->
                        val mappedFavorites = firebaseList.map { mapa ->

                            val tituloStr = mapa["titulo"]?.toString() ?: mapa["title"]?.toString() ?: mapa["name"]?.toString() ?: "Sin título"
                            val imagenStr = mapa["imagenUrl"]?.toString() ?: mapa["imageUrl"]?.toString() ?: mapa["background_image"]?.toString() ?: mapa["imagen"]?.toString() ?: mapa["image"]?.toString() ?: ""
                            // Obtenemos el género (sucio o limpio)
                            val generoCrudo = mapa["generos"]?.toString() ?: mapa["genero"]?.toString() ?: mapa["genre"]?.toString() ?: "Sin género"

                            // EL LIMPIADOR: Borramos corchetes, llaves y la palabra "name="
                            val generoLimpio = generoCrudo
                                .replace("[", "")
                                .replace("]", "")
                                .replace("{", "")
                                .replace("}", "")
                                .replace("name=", "")

                            val idNum = mapa["id"]?.toString()?.toIntOrNull() ?: mapa["gameId"]?.toString()?.toIntOrNull() ?: 0

                            Game(
                                id = idNum,
                                titulo = tituloStr,
                                imagenUrl = imagenStr,
                                generos = listOf(com.example.gamehealthmanager.core.model.Genre(generoLimpio)),
                                descripcion = "Información guardada en tu perfil de GameVault."
                            )
                        }
                        favoritesAdapter.submitList(mappedFavorites)
                    }
                }

                // Observar Todos los Juegos
                launch {
                    viewModel.allGames.collect { firebaseList ->
                        val mappedAllGames = firebaseList.map { mapa ->

                            val tituloStr = mapa["titulo"]?.toString() ?: mapa["title"]?.toString() ?: mapa["name"]?.toString() ?: "Sin título"
                            val imagenStr = mapa["imagenUrl"]?.toString() ?: mapa["imageUrl"]?.toString() ?: mapa["background_image"]?.toString() ?: ""

                            val generoCrudo = mapa["generos"]?.toString() ?: mapa["genero"]?.toString() ?: mapa["genre"]?.toString() ?: "Sin género"

                            val generoLimpio = generoCrudo
                                .replace("[", "")
                                .replace("]", "")
                                .replace("{", "")
                                .replace("}", "")
                                .replace("name=", "")

                            val idNum = mapa["id"]?.toString()?.toIntOrNull() ?: mapa["gameId"]?.toString()?.toIntOrNull() ?: 0

                            Game(
                                id = idNum,
                                titulo = tituloStr,
                                imagenUrl = imagenStr,
                                generos = listOf(com.example.gamehealthmanager.core.model.Genre(generoLimpio)),
                                descripcion = "Información guardada en tu perfil de GameVault."
                            )
                        }
                        gamesAdapter.submitList(mappedAllGames)
                    }
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}