package com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.learning.orderfoodappsch3.core.ViewHolderBinder
import com.learning.orderfoodappsch3.databinding.ItemLinearCategoriesBinding
import com.learning.orderfoodappsch3.model.Category

class CategoryViewHolder(
    private val binding: ItemLinearCategoriesBinding,
    val onCategoryClick: (Category) -> Unit
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Category> {

    override fun bind(item: Category) {
        with(item){
            itemView.setOnClickListener { onCategoryClick(this) }
            binding.tvCategories.text = item.nameCategory
            binding.ivCategories.load(item.imgCategory){
                crossfade(true)
            }
        }
    }
}