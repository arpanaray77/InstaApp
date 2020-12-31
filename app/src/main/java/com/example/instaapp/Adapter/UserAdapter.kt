package com.example.instaapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.Model.User
import com.example.instaapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import android.view.View as AndroidViewView

//import com.google.firebase.database.core.view.View

class UserAdapter (private var mContext:Context,
                   private var mUser:List<User>,
                   private var isFragment:Boolean=false):RecyclerView.Adapter<UserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        //to make user item available in search item
      val view=LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
         return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        //to display the user data
        val user=mUser[position]

        holder.userNameTextView.text=user.getUsername()
        holder.userFullnameTextView.text=user.getFullname()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userProfileImage) //add picasso dependency for image caching and downloading

    }

    class ViewHolder(@NonNull itemView: AndroidViewView): RecyclerView.ViewHolder(itemView)
    {
        var userNameTextView:TextView=itemView.findViewById(R.id.user_item_search_username)
        var userFullnameTextView:TextView=itemView.findViewById(R.id.user_item_search_fullname)
        var userProfileImage:CircleImageView=itemView.findViewById(R.id.user_item_image)
        var followButton: Button =itemView.findViewById(R.id.user_item_follow)
    }

}