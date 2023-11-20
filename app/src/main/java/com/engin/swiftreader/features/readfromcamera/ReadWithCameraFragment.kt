package com.engin.swiftreader.features.readfromcamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.engin.swiftreader.databinding.FragmentReadWithCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@ExperimentalGetImage
@AndroidEntryPoint
class ReadWithCameraFragment : Fragment() {
    private var _binding: FragmentReadWithCameraBinding? = null
    private val binding get() = _binding!!
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var analysisUseCase: ImageAnalysis
    private lateinit var navController: NavController

    @Inject
    lateinit var swiftReaderTextAnalyzer: SwiftReaderTextAnalyzer


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            handlePermission()
        else
            navController.popBackStack()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadWithCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        handlePermission()
    }

    private fun checkPermission() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun handlePermission() {
        if (checkPermission()) {
            startCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider?.unbindAll()

                // Bind use cases to camera
                cameraProvider?.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview,analysisUseCase)

            } catch(exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }
    private fun bindUI() {
        navController = findNavController()
        bindListeners()
        bindCamera()
    }

    private fun bindListeners() {
        binding.cancelImageButton.setOnClickListener { navController.popBackStack() }
        binding.retakeImageButton.setOnClickListener {
            // TODO Delete memory image and refresh again
        }
        binding.validateImageButton.setOnClickListener {
            // TODO Take text from image and save it to database
        }
    }

    private fun bindCamera() {
        swiftReaderTextAnalyzer.onSuccessListener = {
            Log.d("Text Readed",it)
        }
        swiftReaderTextAnalyzer.onErrorListener = {
            Log.d("Text Error", it.localizedMessage?.toString() ?: "")
        }
        analysisUseCase = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(Executors.newSingleThreadExecutor(), swiftReaderTextAnalyzer) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}