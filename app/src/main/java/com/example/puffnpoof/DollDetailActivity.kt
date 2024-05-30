package com.example.puffnpoof

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bumptech.glide.Glide
import com.example.puffnpoof.Model.Transaction
import com.example.puffnpoof.database.DatabaseHelper


class DollDetailActivity : AppCompatActivity(){

    lateinit var btnBack: ImageButton
    lateinit var ivCover: ImageView
    lateinit var tvName: TextView
    lateinit var tvSize: TextView
    lateinit var tvPrice: TextView
    lateinit var tvRating: TextView
    lateinit var tvDesc: TextView
    lateinit var etQty: EditText
    lateinit var btnBuy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doll_detail)

        // global variable
        val global = application as GlobalVar

        // backbutton
        btnBack = findViewById(R.id.ib_back)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // get selected doll passed by intent
        val intent = intent.getIntExtra("dollID", 0)
        val db = DatabaseHelper(this);
        val instance = db.getDoll(intent)

        // populate placeholder with data
        ivCover = findViewById(R.id.iv_cover)
        tvName = findViewById(R.id.tv_name)
        tvSize = findViewById(R.id.tv_size)
        tvPrice = findViewById(R.id.tv_price)
        tvRating = findViewById(R.id.tv_rating)
        tvDesc = findViewById(R.id.tv_desc)

        if (instance != null) {
            tvName.text = instance.name
            tvSize.text = instance.size
            tvPrice.text = global.formatIDPrice(instance.price)
            tvRating.text = instance.rating.toString()
            Glide.with(this).load(instance.imgDir).into(ivCover)
            tvDesc.text = instance.imgDesc
        }

        //validate edittext
        etQty = findViewById(R.id.et_quantity)
        btnBuy = findViewById(R.id.btn_buy)


        btnBuy.setOnClickListener {
            if (validateQty(global)) {
                val userid = global.activeUser?.userID.toString().toInt()
                val qty = etQty.text.toString().toInt()
                val date = global.getCurrentLocalDateString()

                val db = DatabaseHelper(this)
                db.insertTransaction(Transaction(0, userid,intent,qty,date))

                global.isTransaction = true
                etQty.setText("")

                if (instance != null) {
                    showConfirmationDialog(instance.name, qty)
                };

            }
        }

    }


    private fun showConfirmationDialog(name: String, qty: Int) {
        Toast.makeText(this, "$qty $name successfully bought!", Toast.LENGTH_SHORT).show()
    }

    private fun validateQty(global: GlobalVar): Boolean {
        if (etQty.text.toString().isEmpty() || !global.isNumeric(etQty.text.toString())) {
            Toast.makeText(this, "Quantity must be a valid number", Toast.LENGTH_SHORT).show()
            return false
        }
        val qty = etQty.text.toString().toInt()
        if (qty <= 0) {
            Toast.makeText(this, "Quantity must be more than 0", Toast.LENGTH_SHORT).show()
            return false
        }
        hideKeyboard()
        return true
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}