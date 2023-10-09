package com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.learning.orderfoodappsch3.core.ViewHolderBinder
import com.learning.orderfoodappsch3.databinding.ItemGridOrderFoodBinding
import com.learning.orderfoodappsch3.databinding.ItemLinearOrderFoodBinding
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.AdapterLayoutMode
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.viewholder.GridOrderFoodItemViewHolder
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.viewholder.LinearOrderFoodItemViewHolder

class OrderFoodAdapter(
    private val onListOrderFoodClicked: (OrderFood) -> Unit,
    var modeAdapterLayout: AdapterLayoutMode
) : RecyclerView.Adapter<ViewHolder>() {

    private val dataDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<OrderFood>(){
        override fun areItemsTheSame(oldItem: OrderFood, newItem: OrderFood): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderFood, newItem: OrderFood): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType){
            AdapterLayoutMode.GRID.ordinal -> {
                GridOrderFoodItemViewHolder(
                    binding = ItemGridOrderFoodBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), onListOrderFoodClicked
                )
            }
            else -> {
                LinearOrderFoodItemViewHolder(
                    binding = ItemLinearOrderFoodBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), onListOrderFoodClicked
                )
            }
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<OrderFood>).bind(dataDiffer.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return modeAdapterLayout.ordinal
    }

    fun submitData(data: List<OrderFood>){
        dataDiffer.submitList(data)
    }

    fun refreshList(){
        notifyItemRangeChanged(0,dataDiffer.currentList.size)
    }
}