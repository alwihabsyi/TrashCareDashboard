package com.p2mw.trashcaredashboard.ui.tacommerce.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.p2mw.trashcaredashboard.databinding.ItemCommerceOrderBinding
import com.p2mw.trashcaredashboard.model.tacommerce.Order
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.model.tacommerce.getOrderStatus
import com.p2mw.trashcaredashboard.ui.tacommerce.TaCommerceDetailActivity
import com.p2mw.trashcaredashboard.utils.Constants.ORDER
import java.text.SimpleDateFormat
import java.util.Locale

class OrdersAdapter :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(private val binding: ItemCommerceOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Order) {
            binding.apply {
                tvIdPesanan.text = "ID: ${item.orderId}"
                tvStatusPesanan.text = when (getOrderStatus(item.orderStatus)) {
                    is OrderStatus.OnGoing -> "On Process"
                    is OrderStatus.Declined -> "Declined"
                    else -> "Done"
                }
                tvAlamatUser.text = item.address.namaJalan
                tvWaktuPengambilan.text = SimpleDateFormat(
                    "yyyy-MM-dd | hh:mm",
                    Locale.ENGLISH
                ).format(item.date)

                var text = ""
                item.products.forEach {
                    text += if (item.products.last() == it) {
                        it.product.name
                    } else {
                        "${it.product.name}, "
                    }
                }

                tvJenisSampah.text = "Produk: $text"
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            ItemCommerceOrderBinding.inflate(
                LayoutInflater.from(parent.context), null, false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            Intent(context, TaCommerceDetailActivity::class.java).also {
                it.putExtra(ORDER, item)
                context.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}