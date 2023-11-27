package com.engin.swiftreader.features.readfromcamera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil

class TextWrapper : View {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    var boxes = listOf<RectF>()

    var PREVIEW_WIDTH = 1f
    var PREVIEW_HEIGHT = 1f

    //region Constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    override fun onDraw(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (boxes.isNotEmpty()) {
            boxes.forEach { originalRect ->
                canvas.drawRect(originalRect, paint)
            }
        }
    }

    fun scaleRectangles(width: Float, height: Float, rectangles: List<Rect>, image: Rect? = null,imageHeights : Int = 0,imageWidths : Int = 0) {
        val imageHeight = image?.height() ?: imageHeights
        val imageWidth = image?.width() ?: imageWidths

        val scaleX = width / imageHeight
        val scaleY = height / imageWidth

        val scale = scaleX.coerceAtLeast(scaleY)

        val centerX = width / 2

        val offsetX = (width - ceil(imageHeight * scale)) / 2f
        val offsetY = (height - ceil(imageWidth * scale)) / 2f

        if (rectangles.isNotEmpty()) {
            boxes = rectangles.map { originalRect ->
                val newRect = RectF(
                   /*  left = */ originalRect.left * scale + offsetX,
                    /* top = */ originalRect.top * scale + offsetY,
                    /* right = */ originalRect.right * scale + offsetX,
                    /* bottom = */ originalRect.bottom * scale + offsetY
                )
                newRect.apply {
                    left = (centerX + (centerX - left))
                    right = (centerX - (right - centerX))
                }
            }
        }
    }


}