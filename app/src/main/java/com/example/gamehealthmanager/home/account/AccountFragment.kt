package com.example.gamehealthmanager.home.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamehealthmanager.R
import com.example.gamehealthmanager.databinding.FragmentAccountBinding
import com.example.gamehealthmanager.onboarding.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AccountFragment : Fragment(R.layout.fragment_account) {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        // Carga de datos
        viewModel.loadUserData()

        // Observar Perfil
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.userData.collect { data ->
                    data?.let {
                        val fName = it["firstName"] as? String ?: ""
                        val lName = it["lastName"] as? String ?: ""
                        binding.tvTitle.text = "Hola, ${it["userName"] as? String ?: "Usuario"}"
                        binding.tvUserName.text = "$fName $lName"
                        binding.tvUserEmail.text = FirebaseAuth.getInstance().currentUser?.email ?: "Sin correo"
                        binding.tvUserPhone.text = it["phone"] as? String ?: "Sin número"
                        binding.tvUserBirth.text = it["birthDate"] as? String ?: "DD/MM/AAAA"
                    }
                }
            }
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            viewModel.logOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}