package com.engin.swiftreader.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.engin.swiftreader.databinding.FragmentHomeBinding
import com.engin.swiftreader.features.home.adapter.HomeBannerAdapter
import com.engin.swiftreader.features.home.adapter.HomeMainRecyclerViewAdapter
import com.engin.swiftreader.features.home.util.HomeMainRecyclerViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var homeMainAdapter: HomeMainRecyclerViewAdapter? = null
    private var bannerAdapter :HomeBannerAdapter?=null

    private val bannerList = listOf(1,2,3,4)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeData()
    }

    override fun onPause() {
        super.onPause()
        binding.bannerSlider.unRegisterBanner()
    }

    override fun onResume() {
        super.onResume()
        binding.bannerSlider.registerBanner()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.bannerSlider.unRegisterBanner()
        binding.homeMainRecyclerView.adapter = null
        _binding = null
    }


    private fun bindUI() {
        navController = findNavController()
        bindRecyclerView()
        bindBanner()
    }

    private fun bindBanner() {
        bannerAdapter = HomeBannerAdapter(bannerList)
         binding.bannerSlider.initializeBanner(bannerList, bannerAdapter!!)
    }

    private fun bindRecyclerView() {
        binding.homeMainRecyclerView.apply {
            homeMainAdapter = HomeMainRecyclerViewAdapter(
                listOf(
                    HomeMainRecyclerViewHolder.SectionTitle("Discover"),
                    HomeMainRecyclerViewHolder.HorizontalSection(listOf(1, 2, 3)),
                    HomeMainRecyclerViewHolder.SectionTitle("Latest"),
                    HomeMainRecyclerViewHolder.LatestItem(1),
                    HomeMainRecyclerViewHolder.LatestItem(2),
                    HomeMainRecyclerViewHolder.LatestItem(3),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                    HomeMainRecyclerViewHolder.LatestItem(4),
                )
            ) {
                navController.navigate(HomeFragmentDirections.actionHomeFlowToPlayFragment())
            }
            adapter = homeMainAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeData() {
        /*    viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED){
                    launch {

                    }

                    launch {

                    }
                }

                repeatOnLifecycle(Lifecycle.State.STARTED){

                }
            }*/
    }

}