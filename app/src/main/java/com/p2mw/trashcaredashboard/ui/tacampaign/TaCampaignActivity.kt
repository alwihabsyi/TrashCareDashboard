package com.p2mw.trashcaredashboard.ui.tacampaign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.p2mw.trashcaredashboard.databinding.ActivityTaCampaignBinding
import com.p2mw.trashcaredashboard.utils.ViewPagerAdapter

class TaCampaignActivity : AppCompatActivity() {

    private var _binding: ActivityTaCampaignBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabLayout()
    }

    private fun setupTabLayout() {
        val fragments = arrayListOf(
            DailyImpact1Fragment(),
            DailyImpact2Fragment(),
            DailyImpact3Fragment()
        )

        binding.viewPager2.isUserInputEnabled = false

        val viewPagerAdapter = ViewPagerAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Daily Impact 1"
                1 -> tab.text = "Daily Impact 2"
                2 -> tab.text = "Daily Impact 3"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}