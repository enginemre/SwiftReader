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
    var onSuccessListener: (String,List<Rect>,Rect) -> Unit = {_,_,_->}
    var onErrorListener: (Exception) -> Unit = {}

    override fun analyze(image: ImageProxy) {
        image.image?.let { mediaImage ->
            val actualImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            val bitmapImage = image.toBitmap()
            textRecognition.process(actualImage)
                .addOnSuccessListener {
                    val rectangles =  it.textBlocks.mapNotNull { blocks-> blocks.boundingBox }
                    onSuccessListener(it.text,rectangles,image.cropRect)
                }
                .addOnFailureListener { onErrorListener(it) }
                .addOnCompleteListener {
                    mediaImage.close()
                    image.close()
                }
        }
    }
}