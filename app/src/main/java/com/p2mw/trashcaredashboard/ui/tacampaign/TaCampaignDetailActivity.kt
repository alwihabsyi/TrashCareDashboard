package com.p2mw.trashcaredashboard.ui.tacampaign

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.p2mw.trashcaredashboard.R
import com.p2mw.trashcaredashboard.databinding.ActivityTaCampaignDetailBinding
import com.p2mw.trashcaredashboard.model.task.Task
import com.p2mw.trashcaredashboard.ui.tacampaign.viewmodel.CampaignConfirmViewModel
import com.p2mw.trashcaredashboard.utils.Constants.TASK
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.glide
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaCampaignDetailActivity : AppCompatActivity() {

    private var _binding: ActivityTaCampaignDetailBinding? = null
    private val binding get() = _binding!!
    private var task: Task? = null
    private val viewModel by viewModels<CampaignConfirmViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaCampaignDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPage()
        setActions()
        observer()
    }

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    private fun setPage() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(TASK)
        }

        data?.let { taskData ->
            task = taskData
            binding.apply {
                ivProduct.glide(taskData.photoUrl!!)
                tvUserId.text = "User ID: ${taskData.userId}"
                tvTaskId.text = "Task ID: ${taskData.id}"
                tvTaskStatus.text = "Task Status: ${taskData.taskStatus}"

                setTasks(taskData.tugas)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setTasks(tasks: List<String>) {
        binding.apply {
            for (task in tasks) {
                val inflater =
                    LayoutInflater.from(this@TaCampaignDetailActivity)
                        .inflate(R.layout.item_jenis_sampah, null)
                layoutListTask.addView(inflater, layoutListTask.childCount)
            }

            val count = layoutListTask.childCount
            for (c in 0 until count) {
                val v = layoutListTask.getChildAt(c)
                val tvTugas = v.findViewById<TextView>(R.id.tv_jenis_sampah)
                tvTugas.text = tasks[c]
            }

            cvTasks.setOnClickListener {
                if (cvTasksDetail.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cvTasks)
                    cvTasksDetail.hide()
                    icTasksArrow.setImageResource(R.drawable.ic_down)
                } else {
                    TransitionManager.beginDelayedTransition(cvTasks)
                    cvTasksDetail.show()
                    icTasksArrow.setImageResource(R.drawable.ic_keyboardarrowup)
                }
            }
        }
    }

    private fun setActions() {
        binding.btnConfirmOrder.setOnClickListener {
            task?.let {
                viewModel.confirmTask(it)
            }
        }

        binding.btnDeclineOrder.setOnClickListener {
            task?.let {
                viewModel.declineTask(it)
            }
        }
    }

    private fun observer() {
        viewModel.confirmTask.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.buttonLayout.hide()
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    toast(it.data.toString())
                    finish()
                }
                is UiState.Error -> {
                    binding.buttonLayout.show()
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}