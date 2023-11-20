package com.engin.swiftreader.features.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.engin.swiftreader.R
import com.engin.swiftreader.databinding.ItemDiscoverBinding

class DiscoverItemViewHolder(private val binding : ItemDiscoverBinding) :RecyclerView.ViewHolder(binding.root) {

    fun bind(data : Any,onClick : () -> Unit){
        binding.discoveryName.text = "Deneme"
        binding.discoveryImageView.setImageResource(R.mipmap.ic_launcher)
        binding.root.setOnClickListener {
            onClick()
        }
    }
}