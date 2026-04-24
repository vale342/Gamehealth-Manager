package com.example.gamehealthmanager

import android.util.Log
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamehealthmanager.databinding.FragmentRegisterBinding

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.btnRegister.setOnClickListener {
            Log.d("VALE_DEBUG", "Botón Registro presionado")
            val email = binding.tilEmail.editText?.text.toString().trim()
            val password = binding.tilPass.editText?.text.toString().trim()

            Log.d("VALE_DEBUG", "Email: $email, Password: (oculto)")

            if (email.isNotEmpty() && password.length >= 6) {
                viewModel.requestSignUp(email, password)
            } else {
                Log.w("VALE_DEBUG", "Validación fallida: email vacío o pass < 6")
                Toast.makeText(requireContext(), "Revisa tus datos (mínimo 6 caracteres)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpSuccess.collectLatest { success ->
                when (success) {
                    true -> {
                        Toast.makeText(requireContext(), "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment2_to_fragment_registroPersonal)
                        viewModel.resetState()
                    }
                    false -> {
                        Toast.makeText(requireContext(), "Error en el registro. Verifica el Logcat.", Toast.LENGTH_LONG).show()
                        viewModel.resetState()
                    }
                    null -> { /* Sin estado */ }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}