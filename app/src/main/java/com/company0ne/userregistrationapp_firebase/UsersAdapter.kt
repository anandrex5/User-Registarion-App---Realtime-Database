package com.company0ne.userregistrationapp_firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company0ne.userregistrationapp_firebase.databinding.UsersItemBinding


class UsersAdapter(var context: Context, var userList: ArrayList<Users>):RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    //we can directly access the view of the items design
    inner class UsersViewHolder(val adapterBinding:UsersItemBinding) :RecyclerView.ViewHolder(adapterBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UsersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        //in this fun we'll transfer the data in the array to the components
        holder.adapterBinding.textViewName.text = userList[position].userName
        holder.adapterBinding.textViewAge.text = userList[position].userAge.toString() //bcz age is int
        holder.adapterBinding.textViewEmail.text = userList[position].userEmail

        //set onClickListener on itemView
        holder.adapterBinding.linearLayout.setOnClickListener{

            val intent = Intent(context,UpdateUserMainActivity::class.java)
            //send the data object
            intent.putExtra("id",userList[position].userId)
            intent.putExtra("name", userList[position].userName)
            intent.putExtra("age", userList[position].userAge)
            intent.putExtra("email", userList[position].userEmail)
            //we cant startActivity directly so, we use context
            context.startActivity(intent)
        }
    }
    fun getUserId(position: Int): String {
        //return userId which we want to delete
        return userList[position].userId
    }
}