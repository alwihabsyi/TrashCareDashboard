package com.p2mw.trashcaredashboard.ui.tacycle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.model.tacycle.Tacycle
import com.p2mw.trashcaredashboard.utils.Constants.TACYCLE
import com.p2mw.trashcaredashboard.utils.Constants.USER
import com.p2mw.trashcaredashboard.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CycleConfirmViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _confirmOrder = MutableLiveData<UiState<String>>()
    val confirmOrder: LiveData<UiState<String>> = _confirmOrder

    fun confirmOrder(order: Tacycle) {
        _confirmOrder.value = UiState.Loading()
        firestore.runBatch {
            val orderUpdated = order.copy(statusOrder = OrderStatus.Done.orderStatus)

            firestore.collection(TACYCLE).document(order.orderId).set(orderUpdated)
            firestore.collection(USER).document(order.userId!!).collection(TACYCLE).document(order.orderId)
                .set(orderUpdated)
        }.addOnSuccessListener {
            _confirmOrder.value = UiState.Success("Order berhasil disetujui")
        }.addOnFailureListener {
            _confirmOrder.value = UiState.Error(it.message.toString())
        }
    }

    fun declineOrder(order: Tacycle) {
        _confirmOrder.value = UiState.Loading()
        firestore.runBatch {
            val orderUpdated = order.copy(statusOrder = OrderStatus.Declined.orderStatus)

            firestore.collection(TACYCLE).document(order.orderId).set(orderUpdated)
            firestore.collection(USER).document(order.userId!!).collection(TACYCLE).document(order.orderId)
                .set(orderUpdated)
        }.addOnSuccessListener {
            _confirmOrder.value = UiState.Success("Order berhasil ditolak")
        }.addOnFailureListener {
            _confirmOrder.value = UiState.Error(it.message.toString())
        }
    }

}