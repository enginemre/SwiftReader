package com.engin.swiftreader.features.readfromcamera

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject

@ExperimentalGetImage
class SwiftReaderTextAnalyzer @Inject constructor() : ImageAnalysis.Analyzer {

    private val textRecognition = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    var onSuccessListener: (String) -> Unit = {}
    var onErrorListener: (Exception) -> Unit = {}


    override fun analyze(image: ImageProxy) {
        image.image?.let { mediaImage ->
            val actualImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            val bitmapImage = actualImage.bitmapInternal
            textRecognition.process(actualImage)
                .addOnSuccessListener {
                    it.textBlocks.forEach { block ->
                        drawBlockRectangle(
                            bitmapImage,
                            block.boundingBox
                        )
                    }
                    onSuccessListener(it.text)
                }
                .addOnFailureListener { onErrorListener(it) }
                .addOnCompleteListener {
                    mediaImage.close()
                    image.close()
                }
        }
    }

    private fun drawBlockRectangle(bitmapImage: Bitmap?, blockFrame: Rect?) {
        if (bitmapImage != null && blockFrame != null) {
            val canvas = Canvas(bitmapImage)
            canvas.drawBitmap(bitmapImage, null, Rect(0, 0, canvas.width, canvas.height), null)
            canvas.drawRect(blockFrame, paint)
        }
    }
}