package com.example.instaapp.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.Model.User
import com.example.instaapp.R
import de.hdodenhof.circleimageview.CircleImageView

//import com.google.firebase.database.core.view.View

class UserAdapter (private var mContext:Context,
                   private var mUser:List<User>,
                   private var isFragment:Boolean=false):RecyclerView.Adapter<UserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var userNameTextView:TextView=itemView.findViewById(R.id.user_item_search_username)
        var userFullnameTextView:TextView=itemView.findViewById(R.id.user_item_search_fullname)
        var userProfileImage:CircleImageView=itemView.findViewById(R.id.user_item_image)
        var followButton: Button =itemView.findViewById(R.id.user_item_follow)
    }

}