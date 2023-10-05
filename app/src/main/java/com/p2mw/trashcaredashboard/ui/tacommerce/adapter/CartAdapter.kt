package com.p2mw.trashcaredashboard.ui.tacommerce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.p2mw.trashcaredashboard.databinding.ItemCartBinding
import com.p2mw.trashcaredashboard.model.tacommerce.Cart
import com.p2mw.trashcaredashboard.utils.glide
import com.p2mw.trashcaredashboard.utils.toPrice

class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cart: Cart) {
            binding.apply {
                ivProduct.glide(cart.product.photoUrl)
                tvProductName.text = cart.product.name
                tvProductPrice.text = cart.product.price.toPrice()
                tvQuantity.text = "Jumlah: ${cart.quantity}"
            }
        }
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), null, false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = differ.currentList.size
}