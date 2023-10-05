package com.p2mw.trashcaredashboard.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.p2mw.trashcaredashboard.databinding.ItemProductBinding
import com.p2mw.trashcaredashboard.model.tacommerce.Product
import com.p2mw.trashcaredashboard.utils.glide
import com.p2mw.trashcaredashboard.utils.toPrice

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    inner class ProductsViewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                ivProduct.glide(product.photoUrl)
                tvProductName.text = product.name
                tvProductPrice.text = product.price.toPrice()
                tvProductStock.text = product.stock.toString()
            }
        }
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), null, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    var onClick: ((Product) -> Unit)? = null
}