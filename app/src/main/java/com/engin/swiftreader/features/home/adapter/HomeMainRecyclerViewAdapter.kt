package com.engin.swiftreader.features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engin.swiftreader.common.util.AppConstants
import com.engin.swiftreader.databinding.ItemLastestBinding
import com.engin.swiftreader.databinding.ItemSectionTitleBinding
import com.engin.swiftreader.databinding.RowHorizontalSectionBinding
import com.engin.swiftreader.features.home.util.HomeMainRecyclerViewHolder
import com.engin.swiftreader.features.home.viewholder.HorizontalViewHolder
import com.engin.swiftreader.features.home.viewholder.LatestItemViewHolder
import com.engin.swiftreader.features.home.viewholder.SectionTitleViewHolder

class HomeMainRecyclerViewAdapter(
    private val homeList: List<HomeMainRecyclerViewHolder> = emptyList<HomeMainRecyclerViewHolder>(),
    private val onClick : () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AppConstants.LATEST_ITEM_TYPE -> LatestItemViewHolder(
                ItemLastestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            AppConstants.SECTION_TYPE -> SectionTitleViewHolder(
                ItemSectionTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            AppConstants.HORIZONTAL_TYPE -> HorizontalViewHolder(
                RowHorizontalSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onClick
            )
            else -> SectionTitleViewHolder(
                ItemSectionTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount() = homeList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = homeList[position]){
            is HomeMainRecyclerViewHolder.HorizontalSection -> (holder as HorizontalViewHolder).bind(item.data)
            is HomeMainRecyclerViewHolder.LatestItem -> (holder as  LatestItemViewHolder).bind(item.data,onClick)
            is HomeMainRecyclerViewHolder.SectionTitle -> (holder as SectionTitleViewHolder).bind(item.title)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (homeList[position]) {
            is HomeMainRecyclerViewHolder.HorizontalSection -> AppConstants.HORIZONTAL_TYPE
            is HomeMainRecyclerViewHolder.LatestItem -> AppConstants.LATEST_ITEM_TYPE
            is HomeMainRecyclerViewHolder.SectionTitle -> AppConstants.SECTION_TYPE
        }
    }
}