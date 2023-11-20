package com.engin.swiftreader.features.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engin.swiftreader.features.play.ButtonState.END
import com.engin.swiftreader.features.play.ButtonState.IDLE
import com.engin.swiftreader.features.play.ButtonState.PAUSE
import com.engin.swiftreader.features.play.ButtonState.PLAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayViewModel @Inject constructor(

) : ViewModel() {

    private val _wordFlow = MutableSharedFlow<String>(replay = 1, extraBufferCapacity = 1028)
    val wordFlow = _wordFlow.asSharedFlow()

    private val _mode = MutableStateFlow(IDLE)
    val mode = _mode.asStateFlow()

    private val _seekBar = MutableStateFlow(SeekBarState())
    val seekBarState = _seekBar.asStateFlow()

    private val _wordList = MutableStateFlow<List<String>>(emptyList())
    val wordList = _wordList.asStateFlow()

    private val _currentWordIndex = MutableStateFlow<Int>(0)
    val currentWordIndex = _currentWordIndex.asStateFlow()

    private var lock = false


    fun initParagraph(paragraph: String) {
        _wordList.update {
            paragraph.split(" ")
        }
    }


    fun onActionButtonClick() {
        when (mode.value) {
            IDLE -> playRead()
            PAUSE -> playRead()
            PLAY -> pauseRead()
            END -> playRead()
        }
    }

    private fun playRead() {
        viewModelScope.launch {
            _mode.update { PLAY }
            while (wordList.value.size > currentWordIndex.value && mode.value != PAUSE && !lock) {
                _wordFlow.emit(wordList.value[currentWordIndex.value])
                _currentWordIndex.update { currentWordIndex.value + 1 }
                updateSeekBar(currentWordIndex.value)
                delay(1000)
            }
            if (mode.value != PAUSE) {
                _mode.update { END }
                _currentWordIndex.update { 0 }
            }
        }

    }

    private fun pauseRead() {
        _mode.update { PAUSE }
        updateSeekBar(currentWordIndex.value)
    }

    private fun updateSeekBar(progress: Int) {
        _seekBar.update {
            SeekBarState(
                max = _wordList.value.size,
                min = progress,
                progress = progress
            )
        }
    }

    fun onSeekBarChange(progress: Int) {
        _mode.update { PAUSE }
        _seekBar.update {
            SeekBarState(
                max = _wordList.value.size,
                min = progress,
                progress = progress
            )
        }
        _currentWordIndex.update { progress }
        _mode.update { if (progress == 0) IDLE else PLAY }
    }

}

enum class ButtonState {
    IDLE,
    PAUSE,
    PLAY,
    END
}