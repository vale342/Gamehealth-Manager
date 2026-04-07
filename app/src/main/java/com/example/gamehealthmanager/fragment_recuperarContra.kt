package com.example.gamehealthmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gamehealthmanager.databinding.FragmentRecuperarContraBinding

class fragment_recuperarContra : Fragment() {

    private var _binding: FragmentRecuperarContraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecuperarContraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Programar la flecha de volver
        binding.ivBackRecovery.setOnClickListener {
            findNavController().popBackStack()
        }

        // 2. Programar el texto inferior "¿Recordaste tu contraseña?"
        binding.tvBackToLogin.setOnClickListener {
            // Usamos popBackStack para regresar al Login que ya está en la pila
            findNavController().popBackStack()
        }

        // 3. Programar el botón Enviar
        binding.btnRecovery.setOnClickListener {
            // Aquí iría la lógica para enviar el correo más adelante
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}