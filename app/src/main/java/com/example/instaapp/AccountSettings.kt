package com.example.instaapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.instaapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_account_settings.view.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class AccountSettings : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private var checker= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        getUserInfo()

        accountSettings_logoutbtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //forwarding to home page
            val intent = Intent(this@AccountSettings, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
   //editing user information
        save_edited_info.setOnClickListener {
            if (checker == "clicked")
            {

            }
            else
            {
                 updateUserInfoOnly()
            }

        }
    }

    private fun updateUserInfoOnly() {

        val userRef : DatabaseReference =FirebaseDatabase.getInstance().reference.child("Users")
        //using hashmap to store values
        val userMap=HashMap<String,Any>()
        userMap["fullname"]=accountSettings_fullname_profile.text.toString().toLowerCase()
        userMap["username"]=accountSettings_username_profile.text.toString().toLowerCase()
        userMap["bio"]=accountSettings_bio_profile.text.toString().toLowerCase()

    }

    private fun getUserInfo() {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profile_image_profile)
                    accountSettings_fullname_profile?.setText(user.getFullname())
                    accountSettings_username_profile?.setText(user.getUsername())
                    accountSettings_bio_profile?.setText(user.getBio())

                }
            }
        })
    }
}