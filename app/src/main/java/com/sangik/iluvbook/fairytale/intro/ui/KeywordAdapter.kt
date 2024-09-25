package com.sangik.iluvbook.fairytale.intro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.ItemKeywordBinding

class KeywordAdapter(private var keywords: List<String>) : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {
    inner class KeywordViewHolder(val binding: ItemKeywordBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = DataBindingUtil.inflate<ItemKeywordBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_keyword,
            parent,
            false
        )
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.binding.keyword = keywords[position]
    }

    override fun getItemCount() = keywords.size
}
