package com.p2mw.trashcaredashboard.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.p2mw.trashcaredashboard.databinding.FragmentRegisterBinding
import com.p2mw.trashcaredashboard.model.user.User
import com.p2mw.trashcaredashboard.ui.auth.viewmodel.AuthViewModel
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.Validation
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.string
import com.p2mw.trashcaredashboard.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActions()
        observer()
    }

    private fun setActions() {
        binding.apply {
            tvLogin.setOnClickListener {
                findNavController().navigateUp()
            }

            btnRegister.setOnClickListener {
                if (!checkFields()) {
                    toast("Harap isi semua field")
                    return@setOnClickListener
                }

                val user = User(binding.etUsername.string(), binding.etEmail.string())
                viewModel.register(user, etPassword.string())
            }
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.register.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBar.show()
                    }
                    is UiState.Success -> {
                        binding.progressBar.hide()
                        toast("Berhasil mendaftar, silahkan login")
                    }
                    is UiState.Error -> {
                        binding.progressBar.hide()
                        toast(it.error.toString())
                    }
                }
            }

            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect { validation ->
                    if (validation.email is Validation.Failed){
                        withContext(Dispatchers.Main){
                            binding.etEmail.apply {
                                requestFocus()
                                toast(validation.email.message)
                            }
                        }
                    }

                    if (validation.password is Validation.Failed){
                        withContext(Dispatchers.Main){
                            binding.etPassword.apply {
                                requestFocus()
                                toast(validation.password.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkFields() =
        binding.etEmail.string().isNotEmpty() && binding.etPassword.string().isNotEmpty()
                && binding.etUsername.string().isNotEmpty()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}