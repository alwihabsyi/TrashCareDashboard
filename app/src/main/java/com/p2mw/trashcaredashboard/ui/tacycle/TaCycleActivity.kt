package com.p2mw.trashcaredashboard.ui.tacycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.p2mw.trashcaredashboard.databinding.ActivityTaCycleBinding
import com.p2mw.trashcaredashboard.utils.ViewPagerAdapter

class TaCycleActivity : AppCompatActivity() {

    private var _binding: ActivityTaCycleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaCycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabLayout()
    }

    private fun setupTabLayout() {
        val fragments = arrayListOf(
            OnProcessFragment(),
            DoneFragment(),
            DeclinedFragment()
        )

        binding.viewPager2.isUserInputEnabled = false

        val viewPagerAdapter = ViewPagerAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "OnGoing"
                1 -> tab.text = "Done"
                2 -> tab.text = "Declined"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}