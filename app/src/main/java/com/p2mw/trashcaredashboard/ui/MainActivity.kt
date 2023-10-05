package com.p2mw.trashcaredashboard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.p2mw.trashcaredashboard.databinding.ActivityMainBinding
import com.p2mw.trashcaredashboard.ui.auth.AuthActivity
import com.p2mw.trashcaredashboard.ui.products.ProductsActivity
import com.p2mw.trashcaredashboard.ui.tacampaign.TaCampaignActivity
import com.p2mw.trashcaredashboard.ui.tacommerce.TaCommerceActivity
import com.p2mw.trashcaredashboard.ui.tacycle.TaCycleActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setActions()
    }

    private fun setActions() {
        binding.apply {
            btnToProducts.setOnClickListener {
                Intent(this@MainActivity, ProductsActivity::class.java).also {
                    startActivity(it)
                }
            }

            btnToTacampaign.setOnClickListener {
                Intent(this@MainActivity, TaCampaignActivity::class.java).also {
                    startActivity(it)
                }
            }

            btnToTacommerce.setOnClickListener {
                Intent(this@MainActivity, TaCommerceActivity::class.java).also {
                    startActivity(it)
                }
            }

            btnToTacycle.setOnClickListener {
                Intent(this@MainActivity, TaCycleActivity::class.java).also {
                    startActivity(it)
                }
            }

            btnLogout.setOnClickListener {
                auth.signOut()
                Intent(this@MainActivity, AuthActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}