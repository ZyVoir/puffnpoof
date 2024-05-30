package com.example.puffnpoof


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.puffnpoof.navbarFragments.HomeFragment
import com.example.puffnpoof.navbarFragments.ProfileFragment
import com.example.puffnpoof.navbarFragments.TransactionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeFragment = HomeFragment()
        val transactionFragment = TransactionFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)
        val bottomNavigationMenuView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationMenuView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.nav_home -> {
                    setCurrentFragment(homeFragment)
                }
                R.id.nav_transaction ->{
                    setCurrentFragment(transactionFragment)
                }
                R.id.nav_profile -> {
                    setCurrentFragment(profileFragment)
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        showLogOutAlertDialog()
    }

    private fun setCurrentFragment(fragment : Fragment) = supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(
            R.anim.fade_in, // enter animation
            R.anim.fade_out, // exit animation
            R.anim.fade_in, // pop enter animation
            R.anim.fade_out // pop exit animation
        )
        replace(R.id.fl_wrapper, fragment)
        commit()
    }

    fun showLogOutAlertDialog() {
        val builder = AlertDialog.Builder(this@HomeActivity)
        val inflater = this@HomeActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.customalertdialog_2btn,null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        // change info on custom alert dialog
        var title = dialogView.findViewById<TextView>(R.id.tv_title)
        var btn1 = dialogView.findViewById<Button>(R.id.btn_1)
        var btn2 = dialogView.findViewById<Button>(R.id.btn_2)

        title.text = "Confirm Log Out ?"
        btn1.text = "CANCEL"
        btn2.text = "CONFIRM"

        btn1.setOnClickListener {
            alertDialog.dismiss()
        }

        btn2.setOnClickListener {
            val intent = Intent(this, ClosingPage::class.java)
            // Set flags to clear the activity stack
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Start MainActivity
            startActivity(intent)
            // Finish the current activity
            finish()
        }

        // show alertdialog
        alertDialog.show()

    }
}