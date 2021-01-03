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
import com.google.firebase.auth.FirebaseUser
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

        val pref=context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(pref!=null)
        {
            this.profileId= pref.getString("profileId","none")!!
        }
        //to call account profile setting activity
        view.edit_profile_Button.setOnClickListener{
            startActivity(Intent(context,AccountSettings::class.java))
        }
         return view
    }
}