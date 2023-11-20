package com.engin.swiftreader.features.home.util

sealed interface HomeMainRecyclerViewHolder {
    data class HorizontalSection(val data : List<Any>) : HomeMainRecyclerViewHolder
    data class SectionTitle(val title :String) :HomeMainRecyclerViewHolder
    data class LatestItem(val data : Any) :HomeMainRecyclerViewHolder
}