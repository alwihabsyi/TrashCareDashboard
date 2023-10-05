package com.p2mw.trashcaredashboard.ui.tacommerce

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.p2mw.trashcaredashboard.R
import com.p2mw.trashcaredashboard.databinding.ActivityOrderDetailBinding
import com.p2mw.trashcaredashboard.model.tacommerce.Cart
import com.p2mw.trashcaredashboard.model.tacommerce.Order
import com.p2mw.trashcaredashboard.model.user.UserAddress
import com.p2mw.trashcaredashboard.ui.tacommerce.adapter.CartAdapter
import com.p2mw.trashcaredashboard.ui.tacommerce.viewmodel.ConfirmOrderViewModel
import com.p2mw.trashcaredashboard.utils.Constants.ORDER
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.glide
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.toDate
import com.p2mw.trashcaredashboard.utils.toPrice
import com.p2mw.trashcaredashboard.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaCommerceDetailActivity : AppCompatActivity() {

    private var _binding: ActivityOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val cartAdapter by lazy { CartAdapter() }
    private val viewModel by viewModels<ConfirmOrderViewModel>()
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRv()
        setupPage()
        setActions()
        observer()
    }

    private fun setupRv() {
        binding.rvProducts.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(this@TaCommerceDetailActivity)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun setupPage() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ORDER, Order::class.java)
        } else {
            intent.getParcelableExtra(ORDER)
        }

        data?.let {
            order = it
            binding.apply {
                ivBuktiBayar.glide(it.paymentPhoto)
                tvTotalPrice.text = "Total: ${it.totalPrice.toPrice()}"
                tvOrderId.text = "Order ID: ${it.orderId}"
                tvOrderStatus.text = "Order Status: ${it.orderStatus}"
                tvDate.text = "Order Date: ${it.date.toDate()}"

                setupAddress(it.address)
                setupProducts(it.products)
            }
        }
    }

    private fun setupAddress(address: UserAddress) {
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

    private fun setupProducts(products: List<Cart>) {
        binding.apply {
            cartAdapter.differ.submitList(products)

            cvProducts.setOnClickListener {
                if (cvProductsDetail.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cvAddress)
                    cvProductsDetail.hide()
                    icProductsArrow.setImageResource(R.drawable.ic_down)
                }else {
                    TransitionManager.beginDelayedTransition(cvAddress)
                    cvProductsDetail.show()
                    icProductsArrow.setImageResource(R.drawable.ic_keyboardarrowup)
                }
            }
        }
    }

    private fun setActions() {
        binding.apply {
            order?.let { data ->
                btnConfirmOrder.setOnClickListener {
                    viewModel.confirmOrder(data)
                }

                btnDeclineOrder.setOnClickListener {
                    viewModel.declineOrder(data)
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