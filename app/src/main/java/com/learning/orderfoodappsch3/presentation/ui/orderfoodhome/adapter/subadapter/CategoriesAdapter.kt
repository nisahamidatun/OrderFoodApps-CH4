package com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learning.orderfoodappsch3.core.ViewHolderBinder
import com.learning.orderfoodappsch3.databinding.ItemLinearCategoriesBinding
import com.learning.orderfoodappsch3.model.Category
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.viewholder.CategoryViewHolder

class CategoriesAdapter(private val onItemClick: (Category) -> Unit) : RecyclerView.Adapter<CategoryViewHolder>() {
    private val dataDiffer = AsyncListDiffer(this, object: DiffUtil.ItemCallback<Category>(){
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
    })
    fun setItem(items: List<Category>) {
        dataDiffer.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLinearCategoriesBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        (holder as ViewHolderBinder<Category>).bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size



}