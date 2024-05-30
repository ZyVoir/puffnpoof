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
import com.example.puffnpoof.Model.Doll
import com.example.puffnpoof.R
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class recyclerview_homeAdapter(
    private val data : ArrayList<Doll>,
    private val global : GlobalVar,
    private val listener: RecyclerViewEvent,
    val context : Context
) : RecyclerView.Adapter<recyclerview_homeAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val ivCover: ImageView = view.findViewById(R.id.iv_cover)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvPrice: TextView = view.findViewById(R.id.tv_price)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)

        init{
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_home_item, parent, false
        )
        return ItemViewHolder(inflatedView)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val instance : Doll = data[position]

        Glide.with(context).load(instance.imgDir).into(holder.ivCover)
        holder.tvName.text = instance.name
        holder.tvPrice.text = global.formatIDPrice(instance.price)
        holder.tvRating.text = instance.rating.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }



    interface RecyclerViewEvent{
        fun onItemClick(position : Int)
    }
}