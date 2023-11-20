package com.engin.swiftreader.features.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.engin.swiftreader.AuthenticationViewModel
import com.engin.swiftreader.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding : FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel by viewModels<SignUpViewModel>()
    private val authenticationViewModel by activityViewModels<AuthenticationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        binding.signUp.setOnClickListener {
            viewModel.signUpUser(
                email = binding.userTextField.text.toString(),
                password = binding.passwordTextField.text.toString(),
                name = binding.nameSurnameTextField.text.toString()
            )
        }
        binding.signInButton.setOnClickListener {
            authenticationViewModel.signUpWithGoogle()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}