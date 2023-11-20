package com.engin.swiftreader.features.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engin.swiftreader.databinding.ItemDiscoverBinding
import com.engin.swiftreader.databinding.RowHorizontalSectionBinding

class HorizontalViewHolder(
    private val binding: RowHorizontalSectionBinding,
    private val onClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: List<Any>) {
        binding.discoveryRecyclerView.apply {
            adapter = HorizontalAdapter()
        }

    }

    inner class HorizontalAdapter : RecyclerView.Adapter<DiscoverItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverItemViewHolder {
            return DiscoverItemViewHolder(
                ItemDiscoverBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount() = 5

        override fun onBindViewHolder(holder: DiscoverItemViewHolder, position: Int) {
            holder.bind(1,onClick)

        }


    }
}