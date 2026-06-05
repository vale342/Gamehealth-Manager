package com.example.gamehealthmanager.home.gameLists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.databinding.FragmentGameListsBinding
import com.example.gamehealthmanager.home.favorites.FavoritesAdapter
import kotlinx.coroutines.launch

class GameListFragment : Fragment(R.layout.fragment_game_lists) {

    private var _binding: FragmentGameListsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameListsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameListsBinding.bind(view)

        setupRecyclerViews()
        viewModel.loadAllData()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {

                // Observar Favoritos
                launch {
                    viewModel.favoriteGames.collect { games ->
                        binding.rvFavorites.adapter = FavoritesAdapter(games)
                    }
                }

                // Observar Todos los Juegos
                launch {
                    viewModel.allGames.collect { games ->
                        binding.rvGames.adapter = FavoritesAdapter(games)
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