package com.engin.swiftreader.features.readfromcamera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.graphics.toRectF
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.engin.swiftreader.databinding.FragmentReadWithCameraBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalGetImage
@AndroidEntryPoint
class ReadWithCameraFragment : Fragment() {
    private var _binding: FragmentReadWithCameraBinding? = null
    private val binding get() = _binding!!

    /*    private var cameraProvider: ProcessCameraProvider? = null
        private lateinit var analysisUseCase: ImageAnalysis
        private lateinit var capture: ImageCapture*/
    private lateinit var navController: NavController
    private val args: ReadWithCameraFragmentArgs by navArgs()


    private val imageCallBackListener = object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)
            image.image?.let { mediaImage ->
                val actualImage =
                    InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(actualImage)
                    .addOnSuccessListener {
                        val rectangles =
                            it.textBlocks.mapNotNull { blocks -> blocks.boundingBox }
                        binding.overlayView.scaleRectangles(
                            width = binding.overlayView.width.toFloat(),
                            height = binding.overlayView.height.toFloat(),
                            rectangles = rectangles,
                            image = image.cropRect
                        )
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            }
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
        }
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
//        handlePermission()
    }

    /*    override fun onAttach(context: Context) {
            super.onAttach(context)
            WindowInsetsControllerCompat(
                requireActivity().window,
                requireActivity().window.decorView
            ).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        override fun onDetach() {
            super.onDetach()
            WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
                .show(WindowInsetsCompat.Type.systemBars())
        }*/
    /*    private fun checkPermission() =
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
        }*/

    /* private fun startCamera() {
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
                 cameraProvider?.unbindAll()

                 cameraProvider?.bindToLifecycle(
                     viewLifecycleOwner, cameraSelector, preview, capture
                 )

             } catch (exc: Exception) {
                 exc.printStackTrace()
             }

         }, ContextCompat.getMainExecutor(requireContext()))
     }*/

    private fun bindUI() {
        navController = findNavController()
        bindListeners()
        bindCamera()
    }

    private fun bindListeners() {
        binding.cancelImageButton.setOnClickListener { navController.popBackStack() }
        binding.retakeImageButton.setOnClickListener {

        }
        binding.validateImageButton.setOnClickListener {
        }
    }

    private fun bindCamera() {
        binding.resultImage.setImageBitmap(args.image)
        val textRecognition = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        textRecognition.process(InputImage.fromBitmap(args.image, 0))
            .addOnSuccessListener {
                Log.d("Text Readed", it.text)
                val rectangles =  it.textBlocks.mapNotNull { blocks-> blocks.boundingBox }
                _binding?.let {
                    binding.overlayView.boxes = rectangles.map { rect -> rect.toRectF() }
                    binding.overlayView.invalidate()
                }
            }
            .addOnFailureListener { it.printStackTrace() }

//        capture = ImageCapture.Builder().build()

        /*swiftReaderTextAnalyzer.onSuccessListener = { text, rectangles, image ->
            Log.d("Text Readed", text)
            _binding?.let {
                binding.overlayView.scaleRectangles(
                    binding.overlayView.width.toFloat(),
                    binding.overlayView.height.toFloat(),
                    rectangles,
                    image
                )
                binding.overlayView.invalidate()
            }
        }
        swiftReaderTextAnalyzer.onErrorListener = {
            Log.d("Text Error", it.localizedMessage?.toString() ?: "")
        }
        analysisUseCase = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(Executors.newSingleThreadExecutor(), swiftReaderTextAnalyzer) }*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}