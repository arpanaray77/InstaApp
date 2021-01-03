package com.example.instaapp.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.security.identity.AccessControlProfileId
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instaapp.AccountSettings
import com.example.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment: Fragment() {
    private lateinit var profileId:String
    private lateinit var firebaseUser:FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser=FirebaseAuth.getInstance().currentUser!!
        val pref=context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(pref!=null)
        {
            this.profileId= pref.getString("profileId","none")!!
        }

        if(profileId==firebaseUser.uid)
        {
            view.edit_profile_Button.text="Edit Profile"
        }
        else if(profileId==firebaseUser.uid)
        {
            checkFollowOrFollowing()
        }
        //to call account profile setting activity
        view.edit_profile_Button.setOnClickListener{
            startActivity(Intent(context,AccountSettings::class.java))
        }
         return view
    }

    private fun checkFollowOrFollowing() {

        val followingRef=firebaseUser?.uid.let { it1->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        if(followingRef==null)
        {
            followingRef.addValueEventListener(object:ValueEventListener
            {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if(p0.child(profileId).exists())
                    {
                        view?.edit_profile_Button?.text="Following"
                    }
                    else
                    {
                        view?.edit_profile_Button?.text="Follow"
                    }
                }
            })
        }
    }
}