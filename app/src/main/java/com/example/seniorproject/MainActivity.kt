package com.example.seniorproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.seniorproject.databinding.ActivityMainBinding
import com.example.seniorproject.fragments.HomeFragment
import com.example.seniorproject.fragments.MyAdsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fab.setOnClickListener {
            val intent = Intent(this, SellProductActivity::class.java)
            startActivity(intent)
        }


        val navFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        binding.bottomNavigation.background = null

        navController = navFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.chatFragment, R.id.profileFragment, R.id.myAdsFragment
            ), binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)
        setupBottomNavigationView()
        setupActionBarWithNavController(navController, appBarConfiguration)
        loadFragment(HomeFragment())

        binding.navView.setNavigationItemSelectedListener { menuItem -> // Toggle the checked state of menuItem.
            menuItem.isChecked = !menuItem.isChecked
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment()).commit()
                }

                R.id.myAdsFragment -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MyAdsFragment()).commit()
                }
                else -> {
                    val preferences: SharedPreferences =
                        getSharedPreferences("checkbox", MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    editor.putString("remember", "")
                    editor.apply()
//                    FirebaseAuth.getInstance().signOut()

                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                }

            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.homeFragment -> {
                    HomeFragment()
                }
                else -> {
                    MyAdsFragment()
                }
            }
            loadFragment(fragment)
            true
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

}


