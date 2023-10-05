package com.p2mw.trashcaredashboard.ui.tacycle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.model.tacycle.Tacycle
import com.p2mw.trashcaredashboard.utils.Constants.TACYCLE
import com.p2mw.trashcaredashboard.utils.UiState
import javax.inject.Inject

class CycleViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val orderStatus: OrderStatus
): ViewModel() {

    private val _allOrders = MutableLiveData<UiState<List<Tacycle>>>()
    val allOrders: LiveData<UiState<List<Tacycle>>> = _allOrders

    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        _allOrders.value = UiState.Loading()
        firestore.collection(TACYCLE).whereEqualTo("statusOrder", orderStatus.orderStatus)
            .orderBy("tanggalOrder", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _allOrders.value = UiState.Error(error.message.toString())
                    return@addSnapshotListener
                }

                value?.let {
                    val orders = it.toObjects(Tacycle::class.java)
                    _allOrders.value = UiState.Success(orders)
                }
            }
    }

}