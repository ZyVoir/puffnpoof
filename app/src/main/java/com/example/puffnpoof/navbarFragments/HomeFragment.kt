package com.example.puffnpoof.navbarFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.puffnpoof.DollDetailActivity
import com.example.puffnpoof.GlobalVar
import com.example.puffnpoof.Model.Doll
import com.example.puffnpoof.R
import com.example.puffnpoof.adapter.recyclerview_homeAdapter
import com.example.puffnpoof.database.DatabaseHelper
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment(), recyclerview_homeAdapter.RecyclerViewEvent {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    lateinit var usernameView : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up global variable
        val global = requireActivity().application as GlobalVar

        usernameView = view.findViewById(R.id.tv_username)
        usernameView.text = global.activeUser?.username ?: ""

        val recyclerView : RecyclerView = view.findViewById(R.id.rv_dolllist)

        val db = DatabaseHelper(requireContext())


        recyclerView.adapter = recyclerview_homeAdapter(db.getAllDolls(), global,this, requireContext())
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(requireContext(), DollDetailActivity::class.java).apply {
            putExtra("dollID", position+1)
        }

        startActivity(intent)
    }

}