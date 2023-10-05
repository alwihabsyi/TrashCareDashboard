package com.p2mw.trashcaredashboard.ui.tacycle

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.p2mw.trashcaredashboard.R
import com.p2mw.trashcaredashboard.databinding.ActivityTaCycleDetailBinding
import com.p2mw.trashcaredashboard.model.tacycle.Tacycle
import com.p2mw.trashcaredashboard.model.user.UserAddress
import com.p2mw.trashcaredashboard.ui.tacycle.viewmodel.CycleConfirmViewModel
import com.p2mw.trashcaredashboard.utils.Constants.TACYCLE
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.toDate
import com.p2mw.trashcaredashboard.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaCycleDetailActivity : AppCompatActivity() {

    private var _binding: ActivityTaCycleDetailBinding? = null
    private val binding get() = _binding!!
    private var order: Tacycle? = null
    private val viewModel by viewModels<CycleConfirmViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaCycleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPage()
        setActions()
        observer()
    }

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    private fun setupPage() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TACYCLE, Tacycle::class.java)
        } else {
            intent.getParcelableExtra(TACYCLE)
        }

        data?.let { tacycle ->
            order = tacycle
            binding.apply {
                tvUserId.text = "User ID: ${tacycle.userId}"
                tvOrderId.text = "Order ID: ${tacycle.orderId}"
                tvOrderStatus.text = "Order Status: ${tacycle.statusOrder}"
                tvDate.text = "Order Date: ${tacycle.tanggalOrder.toDate()}"
                tvPickupTime.text = "Pickup Time: ${tacycle.waktuPengambilan}"

                setAddress(tacycle.lokasiPengambilan)
                setJenisSampah(tacycle.jenisSampah)
            }
        }
    }

    private fun setAddress(address: UserAddress) {
        binding.apply {
            tvNamaJalan.text = address.namaJalan
            tvKelurahan.text = address.kelurahan
            tvKecamatan.text = address.kecamatan
            tvKota.text = address.kota
            tvProvinsi.text = address.provinsi
            tvKodePos.text = address.kodePos
            tvDetailAlamat.text = address.detailAlamat
            tvJudulAlamat.text = address.judulAlamat

            cvAddress.setOnClickListener {
                if (cvAddressDetail.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cvAddress)
                    cvAddressDetail.hide()
                    icAddressArrow.setImageResource(R.drawable.ic_down)
                }else {
                    TransitionManager.beginDelayedTransition(cvAddress)
                    cvAddressDetail.show()
                    icAddressArrow.setImageResource(R.drawable.ic_keyboardarrowup)
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setJenisSampah(jenisSampah: List<String>) {
        binding.apply {
            for (sampah in jenisSampah) {
                val inflater =
                    LayoutInflater.from(this@TaCycleDetailActivity).inflate(R.layout.item_jenis_sampah, null)
                layoutJenisSampah.addView(inflater, layoutJenisSampah.childCount)
            }

            val count = layoutJenisSampah.childCount
            for (c in 0 until count) {
                val v = layoutJenisSampah.getChildAt(c)
                val tvTugas = v.findViewById<TextView>(R.id.tv_jenis_sampah)
                tvTugas.text = jenisSampah[c]
            }

            cvJenisSampah.setOnClickListener {
                if (cvJenisSampahDetail.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cvJenisSampah)
                    cvJenisSampahDetail.hide()
                    icTrashArrow.setImageResource(R.drawable.ic_down)
                }else {
                    TransitionManager.beginDelayedTransition(cvJenisSampah)
                    cvJenisSampahDetail.show()
                    icTrashArrow.setImageResource(R.drawable.ic_keyboardarrowup)
                }
            }
        }
    }

    private fun setActions() {
        binding.apply {
            order?.let { tacycle ->
                btnConfirmOrder.setOnClickListener {
                    viewModel.confirmOrder(tacycle)
                }

                btnDeclineOrder.setOnClickListener {
                    viewModel.declineOrder(tacycle)
                }
            }
        }
    }

    private fun observer() {
        viewModel.confirmOrder.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.buttonLayout.hide()
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    toast(it.data.toString())
                    finish()
                }
                is UiState.Error -> {
                    binding.buttonLayout.show()
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}