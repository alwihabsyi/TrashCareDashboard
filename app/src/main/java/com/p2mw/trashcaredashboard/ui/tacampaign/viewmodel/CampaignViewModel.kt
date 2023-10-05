package com.p2mw.trashcaredashboard.ui.tacampaign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.p2mw.trashcaredashboard.model.task.Task
import com.p2mw.trashcaredashboard.model.task.TaskGroup
import com.p2mw.trashcaredashboard.utils.Constants
import com.p2mw.trashcaredashboard.utils.UiState
import javax.inject.Inject

class CampaignViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val taskGroup: TaskGroup
) {

    private val _allTasks = MutableLiveData<UiState<List<Task>>>()
    val allTasks: LiveData<UiState<List<Task>>> = _allTasks

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        _allTasks.value = UiState.Loading()
        firestore.collection(Constants.TACYCLE).whereEqualTo("id", taskGroup.taskGroup)
            .orderBy("dateSubmitted", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _allTasks.value = UiState.Error(error.message.toString())
                    return@addSnapshotListener
                }

                value?.let {
                    val orders = it.toObjects(Task::class.java)
                    _allTasks.value = UiState.Success(orders)
                }
            }
    }

}