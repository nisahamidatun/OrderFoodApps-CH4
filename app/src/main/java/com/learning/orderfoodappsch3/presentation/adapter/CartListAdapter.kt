package com.learning.orderfoodappsch3.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.core.ViewHolderBinder
import com.learning.orderfoodappsch3.databinding.ItemFoodCartBinding
import com.learning.orderfoodappsch3.databinding.ItemOrderFoodCartBinding
import com.learning.orderfoodappsch3.model.Cart
import com.learning.orderfoodappsch3.model.CartOrderFood
import com.learning.orderfoodappsch3.utils.doneEditing


interface CartListener {
    fun onIncItemCartClicked(cart: Cart)
    fun onDecItemCartClicked(cart: Cart)
    fun onDelItemCartClicked(cart: Cart)
    fun onDoneEditNotes(cart: Cart)
}

class CartListAdapter(
    private val onCartListener: CartListener? = null
): RecyclerView.Adapter<ViewHolder>(){
    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<CartOrderFood>() {
            override fun areContentsTheSame(
                oldItem: CartOrderFood,
                newItem: CartOrderFood
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areItemsTheSame(oldItem: CartOrderFood, newItem: CartOrderFood): Boolean {
                return oldItem.cart.id == newItem.cart.id && oldItem.orderFood.id == newItem.orderFood.id
            }
        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (onCartListener != null) CartViewHolder(
            ItemFoodCartBinding.inflate(LayoutInflater.from(parent.context), parent, false), onCartListener
        ) else CartOrderViewHolder(
            ItemOrderFoodCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun submitData(data: List<CartOrderFood>) = dataDiffer.submitList(data)

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<CartOrderFood>).bind(dataDiffer.currentList[position])
    }
}

class CartOrderViewHolder(
    private val binding: ItemOrderFoodCartBinding
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<CartOrderFood>{
    override fun bind(item: CartOrderFood) {
        setDataCart(item)
        setNoteCart(item)
    }

    private fun setNoteCart(item: CartOrderFood) {
        binding.tvNote.text = item.cart.notes
    }

    private fun setDataCart(item: CartOrderFood) {
        with(binding){
            tvOrderFoodName.text = item.orderFood.foodName
            binding.ivOrderFood.load(item.orderFood.imgFood){
                crossfade(true)
            }
            tvTotalOrder.text =
                itemView.rootView.context.getString(
                R.string.quantity_total, item.cart.quantityCartItem.toString())
            tvPriceFood.text = (item.cart.quantityCartItem * item.orderFood.foodPrice).toString()
        }
    }

}

class CartViewHolder(
    private val binding: ItemFoodCartBinding,
    private val onCartListener: CartListener?
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<CartOrderFood>{
    private fun setClickListener(item: CartOrderFood) {
        with(binding){
            btnAdd.setOnClickListener{ onCartListener?.onIncItemCartClicked(item.cart) }
            btnMinus.setOnClickListener{ onCartListener?.onDecItemCartClicked(item.cart) }
            ivDel.setOnClickListener { onCartListener?.onDelItemCartClicked(item.cart) }
        }
    }

    private fun setDataCart(item: CartOrderFood){
        with(binding) {
            tvOrderFoodName.text = item.orderFood.foodName
            binding.ivOrderFood.load(item.orderFood.imgFood) {
                crossfade(true)
            }
            tvTotalOrder.text = item.cart.quantityCartItem.toString()
            tvPriceFood.text = (item.cart.quantityCartItem * item.orderFood.foodPrice).toString()
        }
    }

    private fun setNoteCart(item: CartOrderFood) {
        binding.etNotes.setText(item.cart.notes)
        binding.etNotes.doneEditing {
            binding.etNotes.clearFocus()
            val itemNew = item.cart.copy().apply {
                notes = binding.etNotes.text.toString().trim()
            }
            onCartListener?.onDoneEditNotes(itemNew)
        }
    }

    override fun bind(item: CartOrderFood) {
        setClickListener(item)
        setDataCart(item)
        setNoteCart(item)
    }
}


