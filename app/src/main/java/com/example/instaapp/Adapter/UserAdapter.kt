package com.example.instaapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.Fragments.ProfileFragment
import com.example.instaapp.Model.User
import com.example.instaapp.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import android.view.View as AndroidViewView

//import com.google.firebase.database.core.view.View

class UserAdapter (private var mContext:Context,
                   private var mUser:List<User>,
                   private var isFragment:Boolean=false):RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    private val firebaseUser:FirebaseUser?= com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

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

        checkFollowingStatus(user.getUid(),holder.followButton)

        //to go to searched user's profile
        holder.itemView.setOnClickListener { android.view.View.OnClickListener {
            val pref=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
            pref.putString("profileId",user.getUid())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,ProfileFragment()).commit()
        } }

        holder.followButton.setOnClickListener {
            if(holder.followButton.text.toString()=="Follow") {
                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(user.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(user.getUid())
                                        .child("Followers").child(it1.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                            }
                                        }
                                }
                            }
                        }
                }
            }
            else
            {
                if(holder.followButton.text.toString()=="Following") {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(user.getUid())
                            .setValue(true).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firebaseUser?.uid.let { it1 ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow").child(user.getUid())
                                            .child("Followers").child(it1.toString())
                                            .setValue(true).addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                }
                                            }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    class ViewHolder(@NonNull itemView: AndroidViewView): RecyclerView.ViewHolder(itemView)
    {
        var userNameTextView:TextView=itemView.findViewById(R.id.user_item_search_username)
        var userFullnameTextView:TextView=itemView.findViewById(R.id.user_item_search_fullname)
        var userProfileImage:CircleImageView=itemView.findViewById(R.id.user_item_image)
        var followButton: Button =itemView.findViewById(R.id.user_item_follow)
    }

    private fun checkFollowingStatus(uid:String,followButton: Button)
    {
        val followingRef=firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }


        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                if(datasnapshot.child(uid).exists())
                {
                   followButton.text="Following"
                }
                else
                {
                    followButton.text="Follow"
                }
            }

        })
    }

}