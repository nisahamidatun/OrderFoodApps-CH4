package com.learning.orderfoodappsch3.presentation.common.adapter

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
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Cart>() {
            override fun areContentsTheSame(
                oldItem: Cart,
                newItem: Cart
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
                return oldItem.id == newItem.id
            }
        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (onCartListener != null) CartViewHolder(
            ItemFoodCartBinding.inflate(LayoutInflater.from(parent.context), parent, false), onCartListener
        ) else CartOrderViewHolder(
            ItemOrderFoodCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun submitData(data: List<Cart>) = dataDiffer.submitList(data)

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }
}

class CartOrderViewHolder(
    private val binding: ItemOrderFoodCartBinding
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart>{
    override fun bind(item: Cart) {
        setDataCart(item)
        setNoteCart(item)
    }

    private fun setNoteCart(item: Cart) {
        binding.tvNote.text = item.notes
    }

    private fun setDataCart(item: Cart) {
        with(binding){
            tvOrderFoodName.text = item.orderfoodName
            binding.ivOrderFood.load(item.orderfoodImgUrl){
                crossfade(true)
            }
            tvTotalOrder.text =
                itemView.rootView.context.getString(
                    R.string.quantity_total, item.quantityCartItem.toString()
                )
            tvPriceFood.text = (item.quantityCartItem * item.orderfoodPrice).toString()
        }
    }

}

class CartViewHolder(
    private val binding: ItemFoodCartBinding,
    private val onCartListener: CartListener?
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart>{
    private fun setClickListener(item: Cart) {
        with(binding){
            btnAdd.setOnClickListener{ onCartListener?.onIncItemCartClicked(item) }
            btnMinus.setOnClickListener{ onCartListener?.onDecItemCartClicked(item) }
            ivDel.setOnClickListener { onCartListener?.onDelItemCartClicked(item) }
        }
    }

    private fun setDataCart(item: Cart){
        with(binding) {
            tvOrderFoodName.text = item.orderfoodName
            binding.ivOrderFood.load(item.orderfoodImgUrl) {
                crossfade(true)
            }
            tvTotalOrder.text = item.quantityCartItem.toString()
            tvPriceFood.text = (item.quantityCartItem * item.orderfoodPrice).toString()
        }
    }

    private fun setNoteCart(item: Cart) {
        binding.etNotes.setText(item.notes)
        binding.etNotes.doneEditing {
            binding.etNotes.clearFocus()
            val itemNew = item.copy().apply {
                notes = binding.etNotes.text.toString().trim()
            }
            onCartListener?.onDoneEditNotes(itemNew)
        }
    }

    override fun bind(item: Cart) {
        setClickListener(item)
        setDataCart(item)
        setNoteCart(item)
    }
}


