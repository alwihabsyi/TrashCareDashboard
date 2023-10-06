package com.p2mw.trashcaredashboard.ui.products

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.p2mw.trashcaredashboard.databinding.ActivityProductsBinding
import com.p2mw.trashcaredashboard.ui.products.adapter.ProductsAdapter
import com.p2mw.trashcaredashboard.ui.products.viewmodel.ProductsViewModel
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsActivity : AppCompatActivity() {

    private var _binding: ActivityProductsBinding? = null
    private val binding get() = _binding!!
    private val productsAdapter by lazy { ProductsAdapter() }
    private val viewModel by viewModels<ProductsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRv()
        setActions()
        observer()
    }

    private fun setupRv() {
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(this@ProductsActivity)
        }
    }
    
    private fun setActions() {
        binding.btnAddProducts.setOnClickListener {
            Intent(this, ProductDetailActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observer() {
        viewModel.allProducts.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    productsAdapter.differ.submitList(it.data)
                }
                is UiState.Error -> {
                    binding.progressBar.hide()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}