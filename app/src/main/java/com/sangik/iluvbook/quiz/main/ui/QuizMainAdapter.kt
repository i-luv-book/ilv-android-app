package com.sangik.iluvbook.quiz.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.ItemQuizMainBinding
import com.sangik.iluvbook.quiz.main.model.QuizTotalItem

class QuizMainAdapter(private var quizTotalItems : List<QuizTotalItem>): RecyclerView.Adapter<QuizMainAdapter.ItemQuizMainViewHolder>() {
    private val bookCardBgDrawables = listOf(
        R.drawable.bg_bookcard_orange,
        R.drawable.bg_bookcard_green,
        R.drawable.bg_bookcard_blue,
        R.drawable.bg_bookcard_pink,
        R.drawable.bg_bookcard_purple
    )

    private val bookCardBtnDrawables = listOf(
        R.drawable.btn_start_orange,
        R.drawable.btn_start_green,
        R.drawable.btn_start_blue,
        R.drawable.btn_start_pink,
        R.drawable.btn_start_purple
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemQuizMainViewHolder {
        val binding : ItemQuizMainBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_quiz_main, parent, false
        )
        return ItemQuizMainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemQuizMainViewHolder, position: Int) {
        holder.bind(quizTotalItems[position], position)
    }

    override fun getItemCount(): Int = quizTotalItems.size

    fun submitList(newQuizItems : List<QuizTotalItem>) {
        quizTotalItems = newQuizItems
        notifyDataSetChanged()
    }
    inner class ItemQuizMainViewHolder(private val binding: ItemQuizMainBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QuizTotalItem, position: Int) {
            binding.quizItem = item

            val bgDrawable = bookCardBgDrawables[position % bookCardBgDrawables.size]
            val btnDrawable = bookCardBtnDrawables[position % bookCardBtnDrawables.size]

            binding.root.setBackgroundResource(bgDrawable)
            binding.quizItemBtn.setBackgroundResource(btnDrawable)

            binding.executePendingBindings()
        }
    }

}