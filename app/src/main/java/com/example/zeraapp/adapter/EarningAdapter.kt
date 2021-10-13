package com.example.zeraapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.models.EarningPojo
import com.example.zeraapp.models.GetAuthUsers.TransactionPojo
import java.text.SimpleDateFormat
import java.util.*


internal class EarningAdapter(
    var context: Context,
    var data: List<EarningPojo>?
) :
    RecyclerView.Adapter<EarningAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_count : TextView = view.findViewById(R.id.tv_count)
        var tv_principalAmount : TextView = view.findViewById(R.id.tv_principalAmount)
        var tv_interestEarned : TextView = view.findViewById(R.id.tv_interestEarned)
        var tv_finalAmount : TextView = view.findViewById(R.id.tv_finalAmount)

    }


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.earning_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_count.text = data!!.get(position).id
        holder.tv_principalAmount.text = data!!.get(position).principal
        holder.tv_interestEarned.text = data!!.get(position).interest
        holder.tv_finalAmount.text = data!!.get(position).amount

    }




    override fun getItemCount(): Int {
        return data!!.size
    }


}