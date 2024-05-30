package com.example.puffnpoof.navbarFragments

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.puffnpoof.GlobalVar
import com.example.puffnpoof.HomeActivity
import com.example.puffnpoof.R


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    lateinit var usernameView: TextView
    lateinit var emailView: TextView
    lateinit var telpNumView: TextView
    lateinit var btnLogout: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up global variable
        val global = requireActivity().application as GlobalVar

        // populate with current active user
        usernameView = view.findViewById(R.id.tv_username)
        emailView = view.findViewById(R.id.tv_email)
        telpNumView = view.findViewById(R.id.tv_telpnum)

        usernameView.text = global.activeUser?.username ?: ""
        emailView.text = global.activeUser?.email ?: ""
        telpNumView.text = global.activeUser?.phoneNum ?: ""

        // button log out to show confirmation
        btnLogout = view.findViewById(R.id.btn_logout)
        btnLogout.setOnClickListener {
            (activity as HomeActivity).showLogOutAlertDialog()
        }
    }

}