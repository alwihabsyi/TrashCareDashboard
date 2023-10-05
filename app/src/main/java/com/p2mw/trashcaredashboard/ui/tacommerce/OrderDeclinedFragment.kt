package com.p2mw.trashcaredashboard.ui.tacommerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.p2mw.trashcaredashboard.databinding.FragmentDeclinedBinding
import com.p2mw.trashcaredashboard.model.tacommerce.OrderStatus
import com.p2mw.trashcaredashboard.ui.ViewModelFactory
import com.p2mw.trashcaredashboard.ui.tacommerce.adapter.OrdersAdapter
import com.p2mw.trashcaredashboard.ui.tacommerce.viewmodel.OrdersViewModel
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.hide
import com.p2mw.trashcaredashboard.utils.show
import com.p2mw.trashcaredashboard.utils.toast

class OrderDeclinedFragment : Fragment() {

    private var _binding: FragmentDeclinedBinding? = null
    private val binding get() = _binding!!
    private val ordersAdapter by lazy { OrdersAdapter() }
    private val viewModel by viewModels<OrdersViewModel> { ViewModelFactory.getInstance(OrderStatus.Declined) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeclinedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRv()
        observer()
    }

    private fun setupRv() {
        binding.rvOrders.apply {
            adapter = ordersAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observer() {
        viewModel.allOrders.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    it.data?.let { orders ->
                        if (orders.isEmpty()) {
                            binding.tvNoOrder.show()
                            return@observe
                        }

                        ordersAdapter.differ.submitList(orders)
                    }
                }

                is UiState.Error -> {
                    binding.progressBar.hide()
                    toast(it.error.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvNoOrder.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}