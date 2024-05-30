package com.example.puffnpoof.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puffnpoof.GlobalVar
import com.example.puffnpoof.Model.Transaction
import com.example.puffnpoof.R
import com.example.puffnpoof.database.DatabaseHelper


class recyclerview_transactionAdapter(
    private val data : ArrayList<Transaction>,
    private val listener : RecyclerViewEvent,
    val context : Context
) : RecyclerView.Adapter<recyclerview_transactionAdapter.itemViewHolder>() {

    inner class itemViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        val ivCover : ImageView = view.findViewById(R.id.iv_cover)
        val tvId : TextView = view.findViewById(R.id.tv_id)
        val tvDate : TextView = view.findViewById(R.id.tv_date)
        val tvName : TextView = view.findViewById(R.id.tv_name)
        val tvQty : TextView = view.findViewById(R.id.tv_qty)
        val btnUpdate : TextView = view.findViewById(R.id.btn_update)
        val btnDelete : TextView = view.findViewById(R.id.btn_delete)

        init {
            btnUpdate.setOnClickListener(this)
            btnDelete.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_update -> listener.onUpdateBtnClick(tvId.text.toString().substringAfter("TR-").toInt())
                R.id.btn_delete -> listener.onDeleteBtnClick(tvId.text.toString().substringAfter("TR-").toInt())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val inflatedView : View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_transaction_item, parent,false)


        return itemViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {

        val transaction = data[position]
        val db = DatabaseHelper(context)

        val instance = db.getDollInfoFromTransaction(transaction.transactionID)
        if (instance != null) {
            Glide.with(context).load(instance.imgDir).into(holder.ivCover)
            holder.tvName.text = instance.name

        }

        holder.tvId.text = "TR-" + transaction.transactionID.toString()
        holder.tvDate.text = transaction.transactionDate.toString()
        holder.tvQty.text = "Qty : " + transaction.quantity.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RecyclerViewEvent{
        fun onUpdateBtnClick(position: Int)
        fun onDeleteBtnClick(position : Int)
    }
}