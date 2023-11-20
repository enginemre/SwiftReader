package com.engin.swiftreader.features.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.engin.swiftreader.databinding.ItemSectionTitleBinding

class SectionTitleViewHolder(private val binding : ItemSectionTitleBinding) :RecyclerView.ViewHolder(binding.root) {
    fun bind( data :String) {
        binding.title.text = data
    }
}