package com.p2mw.trashcaredashboard.ui.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.p2mw.trashcaredashboard.model.tacommerce.Product
import com.p2mw.trashcaredashboard.utils.Constants.PRODUCTS
import com.p2mw.trashcaredashboard.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _allProducts = MutableLiveData<UiState<List<Product>>>()
    val allProducts: LiveData<UiState<List<Product>>> = _allProducts

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        _allProducts.value = UiState.Loading()
        firestore.collection(PRODUCTS).addSnapshotListener { value, error ->
            if (error != null) {
                _allProducts.value = UiState.Error(error.message.toString())
                return@addSnapshotListener
            }

            val products = value!!.toObjects(Product::class.java)
            _allProducts.value = UiState.Success(products)
        }
    }
}