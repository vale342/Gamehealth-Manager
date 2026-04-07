package com.example.gamehealthmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamehealthmanager.databinding.FragmentRegisterBinding
import androidx.navigation.fragment.findNavController
import com.example.gamehealthmanager.R

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment2_to_fragment_registroPersonal)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ir a la siguiente pantalla (Información Personal)
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment2_to_fragment_registroPersonal)
        }

        // 2. Regresar al Login (Asegúrate de tener un botón o flecha de volver en el XML)
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}