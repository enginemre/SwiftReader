package com.engin.swiftreader.features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engin.swiftreader.R
import com.engin.swiftreader.databinding.ItemBannerBinding

class HomeBannerAdapter(
    private val bannerList : List<Any>
)  : RecyclerView.Adapter<HomeBannerAdapter.HomeBannerViewHolder>()  {


    inner class HomeBannerViewHolder(private val binding: ItemBannerBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.itemBanner.setImageResource(R.mipmap.ic_launcher)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = bannerList.size + 1

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.bind()
    }
}