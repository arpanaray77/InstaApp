package com.example.instaapp.Fragments

import android.R.attr.spacing
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.AccountSettings
import com.example.instaapp.Adapter.MyPostAdapter
import com.example.instaapp.Model.Post
import com.example.instaapp.Model.User
import com.example.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_comment.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment: Fragment() {
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    private var myPostAdapter:MyPostAdapter?=null
    private var postList:MutableList<Post>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId", "none")!!
        }

        if (profileId == firebaseUser.uid) {
            view.edit_profile_Button.text = "Edit Profile"
        } else if (profileId != firebaseUser.uid) {
            checkFollowOrFollowingButtonStatus()
        }
        //to call account profile setting activity
        view.edit_profile_Button.setOnClickListener {
            val getButtontext = view.edit_profile_Button.text.toString()
            when {
                getButtontext == "Edit Profile" -> startActivity(
                    Intent(
                        context,
                        AccountSettings::class.java
                    )
                )

                getButtontext == "Follow" -> {

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId)
                            .setValue(true)

                        pushNotification()
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Followers").child(it1.toString())
                            .setValue(true)
                    }
                }

                getButtontext == "Following" -> {

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId)
                            .removeValue()
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Followers").child(it1.toString())
                            .removeValue()
                    }
                }
            }
        }
        //to get own feeds
        var recyclerView: RecyclerView?=null
        recyclerView=view.findViewById(R.id.recyclerview_profile)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)

        postList=ArrayList()
        myPostAdapter= this.context?.let { MyPostAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter=myPostAdapter

        //to fill in data in profile page
        getFollowers()
        getFollowing()
        getNoofPosts()
        getUserInfo(view)
        myPosts()

        return view
    }

    private fun checkFollowOrFollowingButtonStatus() {

        val followingRef = firebaseUser.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        if (followingRef != null) {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.child(profileId).exists()) {
                        view?.edit_profile_Button?.text = "Following"
                    } else {
                        view?.edit_profile_Button?.text = "Follow"
                    }
                }
            })
        }
    }

    private fun getFollowers() {
        val followersRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    view?.total_followers?.text = snapshot.childrenCount.toString()
                }
            }
        })
    }

    private fun getFollowing() {
        val followingsRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Following")

        followingsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    view?.total_following?.text = snapshot.childrenCount.toString()
                }
            }
        })
    }

    private fun getNoofPosts() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var i:Int=0
                for(snapshot in p0.children)
                {
                    val post=snapshot.getValue(Post::class.java)
                    if(post!!.getPublisher().equals(profileId))
                    {
                        i=i+1
                    }
                }
                view?.total_posts?.text = ""+i
            }
        })
    }

    private fun myPosts() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()
                for(snapshot in p0.children)
                {
                    val post=snapshot.getValue(Post::class.java)
                    if(post!!.getPublisher().equals(profileId))
                        postList!!.add(post)
                }
                Collections.reverse(postList)
                myPostAdapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun pushNotification() {

        val ref = FirebaseDatabase.getInstance().reference.child("Notification").child(profileId)

        val notifyMap = HashMap<String, Any>()
        notifyMap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
        notifyMap["text"] = "started following you "
        notifyMap["postid"] = ""
        notifyMap["ispost"] = true

        ref.push().setValue(notifyMap)
    }


    private fun getUserInfo(view: View) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileId)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(view.profile_image_profile)
                    view.profile_toolbar_username?.text=user.getUsername()
                    view.fullname_in_profile?.text= user.getFullname()
                    view.username_in_profile?.text= user.getUsername()
                    view.bio_profile?.text= user.getBio()

                }
            }
        })
    }

    override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("profileId", firebaseUser.uid)
            pref?.apply()
        }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
}