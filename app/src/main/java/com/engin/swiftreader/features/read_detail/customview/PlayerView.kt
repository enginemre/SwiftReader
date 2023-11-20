package com.engin.swiftreader.features.read_detail.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.engin.swiftreader.R
import com.engin.swiftreader.databinding.LayoutPlayerViewBinding

class PlayerView : ConstraintLayout {

    private var binding: LayoutPlayerViewBinding =
        LayoutPlayerViewBinding.inflate(LayoutInflater.from(context), this)

    //region Constructors
    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    )
    //endregion
    private var onPlayListener : (() -> Unit)? = null

    fun setOnPlayListener(
        onPlayListener : (() -> Unit)? = null
    ){
        this.onPlayListener = onPlayListener
        binding.actionButton.setOnClickListener { onPlayListener?.invoke() }
    }

    fun setPauseMode(){
        binding.actionButton.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_pause) )
    }

    fun setPlayMode(){
        binding.actionButton.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_play) )
    }

    fun setSeekBar(
        min :Int,
        max : Int,
        progress : Int
    ){
        binding.seekBar.min = min
        binding.seekBar.max = max
        binding.seekBar.progress = progress
        binding.totalTime.text = max.toString()
        binding.passedTime.text = progress.toString()
    }

    fun setProgress(progress: Int){
        binding.passedTime.text = progress.toString()
        binding.seekBar.progress = progress
    }

    fun setPassedTime(index : Int){
        binding.passedTime.text = index.toString()
    }

    fun setonSeekBarChangeListener(onSeekBarChange : (Int) -> Unit) {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                onSeekBarChange(p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }
}