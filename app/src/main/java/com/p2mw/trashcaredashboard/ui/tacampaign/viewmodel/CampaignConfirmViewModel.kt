package com.p2mw.trashcaredashboard.ui.tacampaign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.p2mw.trashcaredashboard.model.task.Task
import com.p2mw.trashcaredashboard.model.task.TaskStatus
import com.p2mw.trashcaredashboard.utils.Constants
import com.p2mw.trashcaredashboard.utils.Constants.TASK
import com.p2mw.trashcaredashboard.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CampaignConfirmViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _confirmTask = MutableLiveData<UiState<String>>()
    val confirmTask: LiveData<UiState<String>> = _confirmTask

    fun confirmTask(task: Task) {
        _confirmTask.value = UiState.Loading()
        firestore.runBatch {
            val taskUpdated = task.copy(taskStatus = TaskStatus.Complete.taskStatus)

            firestore.collection(TASK).document(task.taskId).set(taskUpdated)
            firestore.collection(Constants.USER).document(task.userId!!).collection(TASK).document(task.id)
                .set(taskUpdated)
        }.addOnSuccessListener {
            _confirmTask.value = UiState.Success("Order berhasil disetujui")
        }.addOnFailureListener {
            _confirmTask.value = UiState.Error(it.message.toString())
        }
    }

    fun declineTask(task: Task) {
        _confirmTask.value = UiState.Loading()
        firestore.runBatch {
            val taskUpdated = task.copy(taskStatus = TaskStatus.Incomplete.taskStatus)

            firestore.collection(TASK).document(task.taskId).set(taskUpdated)
            firestore.collection(Constants.USER).document(task.userId!!).collection(TASK).document(task.id)
                .set(taskUpdated)
        }.addOnSuccessListener {
            _confirmTask.value = UiState.Success("Order berhasil disetujui")
        }.addOnFailureListener {
            _confirmTask.value = UiState.Error(it.message.toString())
        }
    }

}