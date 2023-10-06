package com.p2mw.trashcaredashboard.ui.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.p2mw.trashcaredashboard.model.tacommerce.Product
import com.p2mw.trashcaredashboard.utils.Constants.PRODUCTS
import com.p2mw.trashcaredashboard.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): ViewModel() {

    private val _saveProduct = MutableLiveData<UiState<Product>>()
    val saveProduct: LiveData<UiState<Product>> = _saveProduct

    private val _deleteProduct = MutableLiveData<UiState<Product>>()
    val deleteProduct: LiveData<UiState<Product>> = _deleteProduct

    fun saveProduct(product: Product) {
        _saveProduct.value = UiState.Loading()
        firestore.collection(PRODUCTS).document(product.id).set(product)
            .addOnSuccessListener {
                _saveProduct.value = UiState.Success(product)
            }
            .addOnFailureListener {
                _saveProduct.value = UiState.Error(it.message.toString())
            }
    }

    fun deleteProduct(product: Product) {
        _deleteProduct.value = UiState.Loading()
        firestore.collection(PRODUCTS).document(product.id).delete()
            .addOnSuccessListener {
                _deleteProduct.value = UiState.Success(product)
            }
            .addOnFailureListener {
                _deleteProduct.value = UiState.Error(it.message.toString())
            }
    }

    fun newProduct(product: Product, photo: ByteArray) {
        _saveProduct.value = UiState.Loading()
        var image = ""
        viewModelScope.launch {
            try {
                async {
                    val id = UUID.randomUUID().toString()
                    launch {
                        val imageStorage = storage.reference.child("task/images/$id")
                        val result = imageStorage.putBytes(photo).await()
                        val downloadUrl = result.storage.downloadUrl.await().toString()
                        image = downloadUrl
                    }
                }.await()
            } catch (e: Exception) {
                _saveProduct.value = UiState.Error(e.message.toString())
            }

            val newProduct = product.copy(photoUrl = image)
            saveProduct(newProduct)
        }
    }
}