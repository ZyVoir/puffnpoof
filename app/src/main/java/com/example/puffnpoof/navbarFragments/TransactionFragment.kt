package com.example.puffnpoof.navbarFragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puffnpoof.GlobalVar
import com.example.puffnpoof.MainActivity
import com.example.puffnpoof.R
import com.example.puffnpoof.adapter.recyclerview_transactionAdapter
import com.example.puffnpoof.database.DatabaseHelper


class TransactionFragment : Fragment(), recyclerview_transactionAdapter.RecyclerViewEvent {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_transaction, container, false)

    lateinit var recyclerView: RecyclerView
    lateinit var tvNoTransaction : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DatabaseHelper(requireContext())
        recyclerView = view.findViewById(R.id.rv_transactionlist)
        recyclerView.adapter =
            recyclerview_transactionAdapter(db.getAllTransaction(),this, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tvNoTransaction = view.findViewById(R.id.tv_notransaction)
        if (db.countTransaction() == 0){
            tvNoTransaction.visibility = View.VISIBLE
        }else {
            tvNoTransaction.visibility = View.INVISIBLE
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


        val db = DatabaseHelper(requireContext())
        recyclerView.adapter =
            recyclerview_transactionAdapter(db.getAllTransaction(),  this, requireContext())
    }

    override fun onUpdateBtnClick(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = this@TransactionFragment.layoutInflater
        val dialogView = inflater.inflate(R.layout.customalertdialog_1btn1form, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        // change info on custom alert dialog
        var title = dialogView.findViewById<TextView>(R.id.tv_title)
        var btn1 = dialogView.findViewById<Button>(R.id.btn_1)
        var btn2 = dialogView.findViewById<Button>(R.id.btn_2)
        var etQty = dialogView.findViewById<EditText>(R.id.et_qty)

        // global var
        val db = DatabaseHelper(requireContext())
        val instance = db.getTransaction(position)


        val global = requireActivity().application as GlobalVar
        val transactionId: String =
            "TR-" + (instance?.transactionID ?: "")
        title.text = "Update $transactionId ?"
        btn1.text = "CANCEL"
        btn2.text = "UPDATE"
        if (instance != null) {
            etQty.setText(instance.quantity.toString())
        }

        btn1.setOnClickListener {
            alertDialog.dismiss()
        }

        btn2.setOnClickListener {
            if (validateQty(etQty, global)) {
                if (instance != null) {
                    db.updateTransaction(instance, etQty.text.toString().toInt())
                }
                recyclerView.adapter =
                    recyclerview_transactionAdapter(db.getAllTransaction(),  this, requireContext())
                Toast.makeText(requireContext(), "$transactionId Updated!", Toast.LENGTH_SHORT)
                    .show()
                alertDialog.dismiss()
            }
        }
        // show alertdialog
        alertDialog.show()
    }

    override fun onDeleteBtnClick(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = this@TransactionFragment.layoutInflater
        val dialogView = inflater.inflate(R.layout.customalertdialog_2btn, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        // global var
        val db = DatabaseHelper(requireContext())
        val instance = db.getTransaction(position)
        // change info on custom alert dialog
        var title = dialogView.findViewById<TextView>(R.id.tv_title)
        var btn1 = dialogView.findViewById<Button>(R.id.btn_1)
        var btn2 = dialogView.findViewById<Button>(R.id.btn_2)

        val global = requireActivity().application as GlobalVar
        val transactionId: String =
            "TR-" + (instance?.transactionID ?: "")
        title.text = "Delete $transactionId ?"
        btn1.text = "CANCEL"
        btn2.text = "DELETE"


        btn1.setOnClickListener {
            alertDialog.dismiss()
        }

        btn2.setOnClickListener {

            val db = DatabaseHelper(requireContext())
            if (instance != null) {
                db.deleteTransaction(instance.transactionID)
            }
            recyclerView.adapter =
                recyclerview_transactionAdapter(db.getAllTransaction(), this, requireContext())
            alertDialog.dismiss()

            Toast.makeText(requireContext(), "$transactionId deleted!", Toast.LENGTH_SHORT).show()
            if (db.countTransaction() == 0){
                tvNoTransaction.visibility = View.VISIBLE
            }else {
                tvNoTransaction.visibility = View.INVISIBLE
            }
        }

        // show alertdialog
        alertDialog.show()
    }

    private fun validateQty(etQty: EditText, global: GlobalVar): Boolean {
        if (etQty.text.toString().isEmpty() || !global.isNumeric(etQty.text.toString())) {
            Toast.makeText(requireContext(), "Quantity must be a valid number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        val qty = etQty.text.toString().toInt()
        if (qty <= 0) {
            Toast.makeText(requireContext(), "Quantity must be more than 0", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

}