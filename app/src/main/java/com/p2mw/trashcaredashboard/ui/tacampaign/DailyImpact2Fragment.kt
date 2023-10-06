package com.p2mw.trashcaredashboard.ui.tacampaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.p2mw.trashcaredashboard.databinding.FragmentDailyImpact2Binding
import com.p2mw.trashcaredashboard.model.task.TaskGroup
import com.p2mw.trashcaredashboard.ui.ViewModelFactory
import com.p2mw.trashcaredashboard.ui.tacampaign.adapter.TaskAdapter
import com.p2mw.trashcaredashboard.ui.tacampaign.viewmodel.CampaignViewModel
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.toast

class DailyImpact2Fragment: Fragment() {

    private var _binding: FragmentDailyImpact2Binding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CampaignViewModel> {
        ViewModelFactory.getTaskInstance(TaskGroup.DailyImpact2)
    }
    private val taskAdapter by lazy { TaskAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyImpact2Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRv()
        observer()
    }

    private fun setupRv() {
        binding.rvTasks.apply {
            adapter = taskAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observer() {
        viewModel.allTasks.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    it.data?.let { orders ->
                        if (orders.isEmpty()) {
                            binding.tvNoTask.show()
                            return@observe
                        }

                        taskAdapter.differ.submitList(orders)
                    }
                }

                is UiState.Error -> {
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}