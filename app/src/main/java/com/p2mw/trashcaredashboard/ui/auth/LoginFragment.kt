package com.p2mw.trashcaredashboard.ui.auth

import android.content.Intent
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
import com.p2mw.trashcaredashboard.R
import com.p2mw.trashcaredashboard.databinding.FragmentLoginBinding
import com.p2mw.trashcaredashboard.ui.MainActivity
import com.p2mw.trashcaredashboard.ui.auth.viewmodel.AuthViewModel
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.Validation
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.setUpForgotPasswordDialog
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.string
import com.p2mw.trashcaredashboard.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActions()
        observer()
    }

    private fun setActions() {
        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPassword.setOnClickListener {
                setUpForgotPasswordDialog {
                    viewModel.resetPassword(it)
                }
            }

            btnLogin.setOnClickListener {
                val email = binding.etEmail.string()
                val password = binding.etPassword.string()
                viewModel.login(email, password)
            }
        }
    }

    private fun observer() {
        viewModel.resetPassword.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Success -> { toast(it.data.toString()) }
                is UiState.Error -> { toast(it.error.toString()) }
            }
        }

        viewModel.login.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    Intent(requireActivity(), MainActivity::class.java).also { intent ->
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                is UiState.Error -> {
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect { validation ->
                    if (validation.email is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.etEmail.apply {
                                requestFocus()
                                toast(validation.email.message)
                            }
                        }
                    }

                    if (validation.password is Validation.Failed) {
                        withContext(Dispatchers.Main) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}