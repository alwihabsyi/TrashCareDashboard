package com.p2mw.trashcaredashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.model.task.TaskGroup
import com.p2mw.trashcaredashboard.ui.tacampaign.viewmodel.CampaignViewModel
import com.p2mw.trashcaredashboard.ui.tacommerce.viewmodel.OrdersViewModel
import com.p2mw.trashcaredashboard.ui.tacycle.viewmodel.CycleViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val orderStatus: OrderStatus? = null,
    private var taskGroup: TaskGroup? = null
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(OrdersViewModel::class.java) -> return OrdersViewModel(
                firestore,
                orderStatus!!
            ) as T

            modelClass.isAssignableFrom(CycleViewModel::class.java) -> return CycleViewModel(
                firestore,
                orderStatus!!
            ) as T

            modelClass.isAssignableFrom(CampaignViewModel::class.java) -> return CampaignViewModel(
                firestore,
                taskGroup ?: TaskGroup.DailyImpact1
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        fun getInstance(orderStatus: OrderStatus): ViewModelFactory =
            ViewModelFactory(
                Firebase.firestore,
                orderStatus,
                null
            )

        fun getTaskInstance(taskGroup: TaskGroup): ViewModelFactory =
            ViewModelFactory(
                Firebase.firestore,
                null,
                taskGroup
            )
    }

}