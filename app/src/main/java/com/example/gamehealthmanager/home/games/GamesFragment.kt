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
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.core.FragmentCommunicator
import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.databinding.FragmentGamesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get()= _binding!!
    private val viewModel by viewModels<GamesViewModel>()
    private lateinit var communicator: FragmentCommunicator
    private val adapter = GamesAdapter { game ->

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGames.adapter = adapter
        observeState()
        //viewModel.loadGames()
        return binding.root
    }
    fun observeState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.gameState.collect { state ->
                    when(state){
                        ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            Log.i("Games", "Games List: ${state.data}")
                            adapter.submitList(state.data)
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
}