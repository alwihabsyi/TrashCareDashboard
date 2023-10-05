package com.p2mw.trashcaredashboard.ui.tacommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.p2mw.trashcaredashboard.model.tacommerce.Order
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.utils.Constants.ORDER
import com.p2mw.trashcaredashboard.utils.UiState
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val orderStatus: OrderStatus
) : ViewModel() {

    private val _allOrders = MutableLiveData<UiState<List<Order>>>()
    val allOrders: LiveData<UiState<List<Order>>> = _allOrders

    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        _allOrders.value = UiState.Loading()
        firestore.collection(ORDER).whereEqualTo("orderStatus", orderStatus.orderStatus)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _allOrders.value = UiState.Error(error.message.toString())
                    return@addSnapshotListener
                }

                value?.let {
                    val orders = it.toObjects(Order::class.java)
                    _allOrders.value = UiState.Success(orders)
                }
            }
    }

}