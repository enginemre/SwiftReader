package com.engin.swiftreader.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.engin.swiftreader.AuthenticationViewModel
import com.engin.swiftreader.common.util.Destination
import com.engin.swiftreader.common.util.collectLatestWithLifecycle
import com.engin.swiftreader.common.util.collectWithLifecycle
import com.engin.swiftreader.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding :FragmentLoginBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel by viewModels<LoginViewModel>()
    private val authenticationViewModel by activityViewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        observeActions()
    }

    private fun observeActions() {
        collectLatestWithLifecycle(viewModel.destination){
            when(it){
                Destination.SignUp -> navigateSignUp()
                Destination.Home -> navigateHome()
                Destination.SignInWithGoogle -> signInWithGoogle()
                else -> Unit
            }
        }

        collectWithLifecycle(authenticationViewModel.destination){
            when(it){
                Destination.Home -> navigateHome()
                else -> Unit
            }
        }

    }

    private fun initializeUI() {
        navController = findNavController()
        binding.login.setOnClickListener{ viewModel.login() }
        binding.signInButton.setOnClickListener{ viewModel.signInWithGoogle()}
    }

    private fun signInWithGoogle(){
        authenticationViewModel.isUserGoogleLoggedIn()
    }

    private fun navigateHome() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph())
    }

    private fun navigateSignUp() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}