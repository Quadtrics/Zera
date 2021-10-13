package com.example.zeraapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.activitys.activity.UpdateAuthUser
import com.example.zeraapp.models.AuthUsers.AuthUsersDatum


internal class
AddUserAdapter(
    var context: Context,
    var data: List<AuthUsersDatum>?
) :
    RecyclerView.Adapter<AddUserAdapter.MyViewHolder>() {

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llAddUserAdapter: LinearLayout = view.findViewById(R.id.llAddUserAdapter)
        var name: TextView = view.findViewById(R.id.authUserName)
    }


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_user_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.llAddUserAdapter.setOnClickListener {
            var intent = Intent(context,UpdateAuthUser::class.java)
            intent.putExtra("authUserId",data!!.get(position)._id)
            context.startActivity(intent)
        }

        holder.name.text = data!!.get(position).name

    }


    override fun getItemCount(): Int {
        return data!!.size
    }


}