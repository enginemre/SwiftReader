package com.engin.swiftreader.features.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.engin.swiftreader.R
import com.engin.swiftreader.common.util.collectLatestWithLifecycle
import com.engin.swiftreader.common.util.collectWithLifecycle
import com.engin.swiftreader.databinding.FragmentPlayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val viewModel: PlayViewModel by viewModels()
    val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeData()
    }

    private fun observeData() {
        collectLatestWithLifecycle(viewModel.wordFlow) {
            binding.mainWord.text = it
        }

        collectLatestWithLifecycle(viewModel.seekBarState) {
            binding.playerView.setProgress(it.progress)
            binding.playerView.setPassedTime(it.progress)
        }

        collectWithLifecycle(viewModel.mode) {
            with(binding.playerView) {
                if (it == ButtonState.PLAY) setPauseMode() else setPlayMode()
            }
        }
    }

    private fun bindUI() {
        bindPlayerView()
    }

    private fun bindPlayerView() {
        viewModel.initParagraph(resources.getString(R.string.lorem_ipsum))
        binding.playerView.setOnPlayListener(viewModel::onActionButtonClick)
        binding.playerView.setonSeekBarChangeListener(viewModel::onSeekBarChange)
        binding.playerView.setSeekBar(min = 0, max = viewModel.wordList.value.size, progress = 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}