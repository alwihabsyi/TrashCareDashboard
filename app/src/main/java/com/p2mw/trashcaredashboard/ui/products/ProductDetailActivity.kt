package com.p2mw.trashcaredashboard.ui.products

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.p2mw.trashcaredashboard.databinding.ActivityProductDetailBinding
import com.p2mw.trashcaredashboard.model.tacommerce.Product
import com.p2mw.trashcaredashboard.ui.products.viewmodel.ProductDetailViewModel
import com.p2mw.trashcaredashboard.utils.Constants.PRODUCTS
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.glide
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.reduceFileImage
import com.p2mw.trashcaredashboard.utils.setUpGallery
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.string
import com.p2mw.trashcaredashboard.utils.toast
import com.p2mw.trashcaredashboard.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {

    private var _binding: ActivityProductDetailBinding? = null
    private val binding get() = _binding!!
    private var product: Product? = null
    private val viewModel by viewModels<ProductDetailViewModel>()
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPage()
        setActions()
        observer()
    }

    @Suppress("DEPRECATION")
    private fun setupPage() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PRODUCTS, Product::class.java)
        } else {
            intent.getParcelableExtra(PRODUCTS)
        }

        data?.let {
            product = it
            binding.apply {
                tvAddPhoto.hide()
                ivProduct.glide(it.photoUrl)
                btnDeleteProduct.show()
                etProductName.setText(it.name)
                etProductPrice.setText(it.price.toString())
                etProductStock.setText(it.stock.toString())
                etProductDescription.setText(it.description)
            }
        }
    }

    private fun setActions() {
        binding.apply {
            btnSaveProduct.setOnClickListener {
                val newProduct = Product(
                    etProductName.string(),
                    etProductPrice.string().toDouble(),
                    etProductStock.string().toInt(),
                    etProductDescription.string()
                )

                if (product != null) {
                    if (imageFile == null) {
                        val productEdited = newProduct.copy(
                            photoUrl = product!!.photoUrl,
                            id = product!!.id
                        )
                        viewModel.saveProduct(productEdited)
                    }else {
                        val setPhoto = reduceFileImage(imageFile as File)
                        val photoByteArray = setPhoto.readBytes()
                        val productEdited = newProduct.copy(
                            photoUrl = product!!.photoUrl,
                            id = product!!.id
                        )
                        viewModel.newProduct(productEdited, photoByteArray)
                    }
                    return@setOnClickListener
                }

                if (!checkFields()) {
                    toast("Harap isi semua field dan foto produk")
                    return@setOnClickListener
                }

                val setPhoto = reduceFileImage(imageFile as File)
                val photoByteArray = setPhoto.readBytes()
                viewModel.newProduct(newProduct, photoByteArray)
            }

            btnDeleteProduct.setOnClickListener {
                product?.let {
                    viewModel.deleteProduct(it)
                }
            }

            cvProduct.setOnClickListener {
                val chooser = setUpGallery()
                launchGallery.launch(chooser)
            }
        }
    }

    private fun observer() {
        viewModel.saveProduct.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.buttonLayout.hide()
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    it.data?.let { product ->
                        toast("Produk ${product.name} berhasil disimpan")
                        finish()
                    }
                }
                is UiState.Error -> {
                    binding.buttonLayout.show()
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }

        viewModel.deleteProduct.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.buttonLayout.hide()
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    it.data?.let { product ->
                        toast("Produk ${product.name} berhasil dihapus")
                        finish()
                    }
                }
                is UiState.Error -> {
                    binding.buttonLayout.show()
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this)
                imageFile = myFile
                binding.tvAddPhoto.hide()
                binding.ivProduct.setImageURI(uri)
            }
        }
    }

    private fun checkFields(): Boolean =
        binding.etProductName.string().isNotEmpty() && binding.etProductDescription.string()
            .isNotEmpty() &&
                binding.etProductPrice.string().isNotEmpty() && binding.etProductStock.string()
            .isNotEmpty() && imageFile != null

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}