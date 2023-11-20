package com.engin.swiftreader.features.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.engin.swiftreader.R
import com.engin.swiftreader.databinding.ItemLastestBinding

class LatestItemViewHolder(private val binding: ItemLastestBinding) :RecyclerView.ViewHolder(binding.root) {
    fun bind(data : Any,onClick : () -> Unit){
        binding.latestItemName.text = "Grinin elli tonu"
        binding.latestItemImage.setImageResource(R.mipmap.ic_launcher)
        binding.root.setOnClickListener {
            onClick()
        }
    }
}