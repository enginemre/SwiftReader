package com.engin.swiftreader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.engin.swiftreader.common.util.Destination
import com.engin.swiftreader.common.util.collectWithLifecycle
import com.engin.swiftreader.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val authenticationViewModel by activityViewModels<AuthenticationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeData()
    }

    private fun observeData() {
        collectWithLifecycle(authenticationViewModel.destination){
            if (it is Destination.Login)
                routeToLogOut()
        }
    }

    private fun routeToLogOut() {
        NavDeepLinkBuilder(requireContext())
            .setGraph(R.navigation.base_graph)
            .setDestination(R.id.splashFragment)
            .createPendingIntent().send()
    }

    private fun bindUI() {
        navController = findNavController()
        binding.buttonSecond.setOnClickListener { authenticationViewModel.signOut() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}