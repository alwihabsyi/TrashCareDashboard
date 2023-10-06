package com.p2mw.trashcaredashboard.model.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Task(
    val taskStatus: String = "",
    val id: String = "",
    val tugas: List<String> = emptyList(),
    val campaignNo: Int = 0,
    val photoUrl: String? = null,
    val userId: String? = null,
    val dateSubmitted: Date? = null,
    val taskId: String = id + userId
): Parcelable