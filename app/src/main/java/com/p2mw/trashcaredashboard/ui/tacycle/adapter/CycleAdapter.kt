package com.p2mw.trashcaredashboard.ui.tacycle.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.p2mw.trashcaredashboard.databinding.ItemCommerceOrderBinding
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.model.tacommerce.getOrderStatus
import com.p2mw.trashcaredashboard.model.tacycle.Tacycle
import com.p2mw.trashcaredashboard.ui.tacycle.TaCycleDetailActivity
import com.p2mw.trashcaredashboard.utils.Constants.TACYCLE
import java.text.SimpleDateFormat
import java.util.Locale

class CycleAdapter :
    RecyclerView.Adapter<CycleAdapter.CycleViewHolder>() {

    inner class CycleViewHolder(private val binding: ItemCommerceOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Tacycle) {
            binding.apply {
                tvIdPesanan.text = "ID: ${item.orderId}"
                tvStatusPesanan.text = when (getOrderStatus(item.statusOrder)) {
                    is OrderStatus.OnGoing -> "On Process"
                    is OrderStatus.Declined -> "Declined"
                    else -> "Done"
                }
                tvAlamatUser.text = item.lokasiPengambilan.namaJalan
                tvWaktuPengambilan.text = SimpleDateFormat(
                    "yyyy-MM-dd | hh:mm",
                    Locale.ENGLISH
                ).format(item.tanggalOrder)

                var text = ""
                item.jenisSampah.forEach {
                    text += if (item.jenisSampah.last() == it) {
                        it
                    } else {
                        "${it}, "
                    }
                }

                tvJenisSampah.text = "Jenis Sampah: $text"
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Tacycle>() {
        override fun areItemsTheSame(oldItem: Tacycle, newItem: Tacycle): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Tacycle, newItem: Tacycle): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CycleViewHolder {
        return CycleViewHolder(
            ItemCommerceOrderBinding.inflate(
                LayoutInflater.from(parent.context), null, false
            )
        )
    }

    override fun onBindViewHolder(holder: CycleViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            Intent(context, TaCycleDetailActivity::class.java).also {
                it.putExtra(TACYCLE, item)
                context.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}