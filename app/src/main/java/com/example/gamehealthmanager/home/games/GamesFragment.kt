package com.example.gamehealthmanager.home.games

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView // <-- IMPORTANTE: Asegúrate de que esto esté importado
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.core.FragmentCommunicator
import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.databinding.FragmentGamesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.SearchView
import com.example.gamehealthmanager.core.model.HealthRating

class GamesFragment : Fragment() {
    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<GamesViewModel>()
    private lateinit var communicator: FragmentCommunicator

    private val adapter = GamesAdapter { game ->
        // ¡FUERZA LA INICIALIZACIÓN AQUÍ!
        if (game.healthRating == null) {
            game.healthRating = HealthRating.NONE
        }

        // Ahora navegamos con seguridad
        val bundle = Bundle().apply { putParcelable("game", game) }
        findNavController().navigate(R.id.action_gamesFragment_to_gameDetailFragment, bundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as FragmentCommunicator
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGames.adapter = adapter

        // --- NUEVO: CÓDIGO DE PAGINACIÓN (INFINITE SCROLL) ---
        binding.rvGames.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // canScrollVertically(1) revisa si aún hay espacio hacia abajo.
                // Si regresa false, significa que el usuario tocó el fondo de la lista.
                if (!recyclerView.canScrollVertically(1)) {
                    // ¡Llegamos al final! Pedimos la siguiente página
                    viewModel.loadGames()
                }
            }
        })
        // -----------------------------------------------------

        // ¡Asegúrate de tener importado androidx.appcompat.widget.SearchView!
        binding.svGames.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchGames(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        observeState()
    }

    fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameState.collect { state ->
                    when (state) {
                        ResponseService.Loading -> communicator.manageLoader(true)
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            // adapter.submitList ya se encarga de actualizar la pantalla
                            // suavemente cuando llegan los nuevos 30 juegos
                            adapter.submitList(state.data.results)
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}