package com.haniifac.capstonepesaing_revisited.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haniifac.capstonepesaing_revisited.databinding.CategoryItemsBinding
import com.haniifac.capstonepesaing_revisited.domain.entity.Category

class CategoryRecyclerAdapter(private val listCategory : ArrayList<Category>): RecyclerView.Adapter<CategoryRecyclerAdapter.MainCategoryHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class MainCategoryHolder(private val binding: CategoryItemsBinding): RecyclerView.ViewHolder(binding.root) {
        val img = binding.imgIconCategory
        val contentTxt = binding.tvIconCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryHolder {
        val binding = CategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainCategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: MainCategoryHolder, position: Int) {
        val (txt, photo) = listCategory[position]
        holder.contentTxt.text = txt
        holder.img.setImageResource(photo)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listCategory[holder.adapterPosition], holder)
        }
    }

    override fun getItemCount(): Int = listCategory.size

    interface OnItemClickCallback {
        fun onItemClicked(category: Category, holder: MainCategoryHolder)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}