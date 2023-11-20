package com.engin.swiftreader.features.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.engin.swiftreader.databinding.LayoutBannerViewBinding
import com.engin.swiftreader.features.home.adapter.HomeBannerAdapter
import kotlin.math.abs

class BannerView : ConstraintLayout {
    //region Constructors
    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    )
    //endregion

    private var binding: LayoutBannerViewBinding =
        LayoutBannerViewBinding.inflate(LayoutInflater.from(context), this)

    private lateinit var bannerList: List<Any>

    private val viewPagerChangeListener = object : ViewPager2.OnPageChangeCallback() {
        var currentIndex = -1

        val runnable = Runnable {
            binding.bannerSlider.setCurrentItem(binding.bannerSlider.currentItem + 1, true)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentIndex = position
            binding.bannerSlider.removeCallbacks(runnable)
            binding.bannerSlider.postDelayed(runnable, DEFAULT_BANNER_TIME)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                if (currentIndex == bannerList.size) {
                    binding.bannerSlider.setCurrentItem(0, false)
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun initializeBanner(bannerList: List<Any>, bannerAdapter: HomeBannerAdapter) {
        this.bannerList = bannerList
        with(binding.bannerSlider) {
            adapter = bannerAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = DEFAULT_OFF_PAGE_LIMIT
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(TRANSFORMER_MARGIN))
                addTransformer { page, position ->
                    page.scaleY = 0.90f + (1 - abs(position) * 0.1f)
                }
            }
            setPageTransformer(compositePageTransformer)
        }
    }

    fun registerBanner() {
        binding.bannerSlider.registerOnPageChangeCallback(viewPagerChangeListener)
    }

    fun unRegisterBanner() {
        binding.bannerSlider.unregisterOnPageChangeCallback(viewPagerChangeListener)
    }

    companion object {
        private const val DEFAULT_OFF_PAGE_LIMIT = 3
        private const val TRANSFORMER_MARGIN = 40
        private const val DEFAULT_BANNER_TIME = 2000L
    }

}