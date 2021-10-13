package com.example.zeraapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.models.GetAuthUsers.TransactionPojo
import com.example.zeraapp.utlis.*



internal class TransactionAdapter(
    var context: Context,
    var data: List<TransactionPojo>?
) :
    RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {



    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_description : TextView = view.findViewById(R.id.tv_description)
        var tv_date : TextView = view.findViewById(R.id.tv_date)
        var tv_amount : TextView = view.findViewById(R.id.tv_amount)
        var civ_transactImage : ImageView = view.findViewById(R.id.civ_transactImage)

    }


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tv_description.text = data!!.get(position).description
        holder.tv_date.text = displayDate(data!!.get(position).date.toString())
             var transactionType:String = data!!.get(position).type.toString()
               setLog("MyTransactionType","  "+transactionType)
        if (transactionType.equals("withdrawal",ignoreCase = false)) {
                   holder.tv_amount.text = moneyWidthdraw(data!!.get(position).amount.toString())
                   holder.tv_amount.setTextColor(getColor(context, R.color.dark_blue))
       /*
            holder.civ_transactImage.rotation= 180F
            holder.civ_transactImage.setColorFilter(getColor(context, R.color.light_red), android.graphics.PorterDuff.Mode.MULTIPLY);
*/          holder.civ_transactImage.background=getDrawable(context, R.drawable.withdraw)
        }else{
            holder.tv_amount.text = moneyCredited(data!!.get(position).amount.toString())
            holder.civ_transactImage.background=getDrawable(context, R.drawable.trangstn_icon)

                   holder.tv_amount.setTextColor(getColor(context, R.color.colorPrimary))
            /*         holder.civ_transactImage.rotation= 0F
                     holder.civ_transactImage.setColorFilter(getColor(context, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
         *//*
 */       }



    }




    override fun getItemCount(): Int {
        return data!!.size
    }

    fun updateList(filteredTransactions: List<TransactionPojo>?) {
        data = filteredTransactions
        notifyDataSetChanged()
    }


}