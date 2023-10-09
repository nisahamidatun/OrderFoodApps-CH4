package com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.orderfoodappsch3.databinding.ItemLinearCategoriesBinding
import com.learning.orderfoodappsch3.model.Category
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.viewholder.CategoryViewHolder

class CategoriesAdapter() : RecyclerView.Adapter<CategoryViewHolder>() {
    private var items: MutableList<Category> = mutableListOf()

    fun setItem(items: List<Category>) {
        this.items.clear()
        this.items.addAll(items)
        notifyItemRangeChanged(0,items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLinearCategoriesBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}