package com.example.gamehealthmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gamehealthmanager.databinding.FragmentRegistroPersonalBinding

class fragment_registroPersonal : Fragment() {

    // 1. Configuramos el Binding igual que en el Login
    private var _binding: FragmentRegistroPersonalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. Programar el botón "Volver" (la flechita blanca)
        binding.ivBackPersonal.setOnClickListener {
            findNavController().popBackStack()
        }

        // 3. Programar el botón "Guardar"
        binding.btnSavePersonal.setOnClickListener {
            // Aquí podrías agregar un Toast o navegar a la pantalla principal después del registro
            // Por ahora, solo lo dejamos listo para el evento
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}