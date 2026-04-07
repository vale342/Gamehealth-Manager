package com.example.gamehealthmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.gamehealthmanager.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.tvRegisterAction2.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }
        binding.tvRegisterAction.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_fragment_recuperarContra)
        }

        return binding.root
    }
    /*Se declaro diferentes binding con _ es una variable y la otra una constante
    Se usará la constante binding para acceder a los elementos de la interfaz de usuario
    ? Significa que puede ser opcional (en memoria el null no significa naa y el vacio si puede estar asiggnado, pero no contiene valor)    */
}
