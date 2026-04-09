package com.example.gamehealthmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener // Permite escuchar cambios en el texto en tiempo real
import androidx.navigation.fragment.findNavController // Necesario para movernos entre fragmentos
import com.example.gamehealthmanager.databinding.FragmentLoginBinding // Clase generada para acceder a las vistas del XML

class LoginFragment : Fragment() {

    // _binding: Variable mutable que puede ser nula. Se usa "?" porque la vista no existe hasta que se infla.
    // En memoria, null indica ausencia de objeto, mientras que un objeto vacío ocupa espacio.
    private var _binding: FragmentLoginBinding? = null

    // binding: Propiedad de solo lectura que garantiza que _binding no sea nulo mediante "!!".
    // Se usa para acceder a los componentes del XML sin usar findViewById.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflamos el diseño del fragmento y lo asignamos al binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Inicializamos la lógica de validación de los campos
        setupValidation()

        // Configuramos la navegación al presionar "Registrarse"
        binding.tvRegisterAction2.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }

        // Configuramos la navegación al presionar "Recuperar contraseña"
        binding.tvRegisterAction.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_fragment_recuperarContra)
        }

        // Retornamos la vista raíz del diseño inflado
        return binding.root
    }

    private fun setupValidation(){
        // Desactivamos el botón al inicio para que el usuario no pueda ingresar datos vacíos o incorrectos
        binding.btnLogin.isEnabled = false

        // ACCESO POR JERARQUÍA: En el XML, solo el TextInputLayout (tilUser) tiene ID.
        // Para obtener el texto, entramos al "padre" (tilUser) y buscamos a su "hijo" editor (.editText).
        // El operador "?." asegura que si el editor no existe, el programa no se detenga (Null Safety).

        binding.tilUser.editText?.addTextChangedListener {
            validateFields() // Cada que el usuario escribe una letra en correo, validamos todo
        }

        binding.tilPass.editText?.addTextChangedListener {
            validateFields() // Cada que el usuario escribe una letra en contraseña, validamos todo
        }
    }

    private fun validateFields(){
        // toString() convierte el contenido a cadena y trim() elimina espacios accidentales al inicio o final
        val email = binding.tilUser.editText?.text.toString().trim()
        val password = binding.tilPass.editText?.text.toString().trim()

        // Verificamos si los datos cumplen con el formato de correo y longitud mínima
        val isEmailValid = isValidEmail(email)
        val isPasswordValid = password.length >= 8

        // GESTIÓN DE ERRORES VISUALES:
        // Si el campo está vacío o es válido, no mostramos error (null). De lo contrario, avisamos al usuario.
        binding.tilUser.error = if (email.isNotEmpty() || isEmailValid) null else "Correo inválido"
        binding.tilPass.error = if (password.isNotEmpty() || isPasswordValid) null else "Mínimo 8 caracteres"

        // ACTIVACIÓN DEL BOTÓN:
        // Solo se vuelve true si AMBOS campos tienen texto Y AMBOS son válidos.
        binding.btnLogin.isEnabled =
            email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid
    }

    //Método auxiliar que usa patrones de Android para verificar si un String tiene forma de email (usuario@dominio.com)
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiamos el binding para liberar memoria, ya que los fragmentos pueden vivir más que sus vistas
        _binding = null
    }
}