package com.example.instaapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        login_link.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        signup_btn.setOnClickListener {
            createAccount()
        }

    }

    private fun createAccount() {
        val fullName=signup_fullname.text.toString()
        val userName=signup_username.text.toString()
        val email=signup_email.text.toString()
        val password=signup_password.text.toString()

        //checking empty fields
        when{
            TextUtils.isEmpty(fullName)-> Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(userName)-> Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(email)-> Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password)-> Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()

            else->
            {
                val progressDialog= ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please wait...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                              saveUserInfo(fullName,userName,email,progressDialog)
                        }
                        else
                        {
                            val message=task.exception!!.toString()
                            Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }

    private fun saveUserInfo(fullName: String, userName: String, email: String,progressDialog:ProgressDialog) {
        val currentUserId=FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference=FirebaseDatabase.getInstance().reference.child("Users")
        //using hashmap to store values
        val userMap=HashMap<String,Any>()
        userMap["uid"]=currentUserId
        userMap["fullname"]=fullName.toLowerCase()
        userMap["username"]=userName.toLowerCase()
        userMap["email"]=email
        userMap["bio"]="Hey I am using InstaApp"
        userMap["image"]="https://firebasestorage.googleapis.com/v0/b/instaapp-5d470.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=b993b78f-0959-48e8-9901-a4ff11bbe21f"

        //pasting data in database
        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this,"Account has been created",Toast.LENGTH_SHORT).show()

                    //to follow own account by default
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentUserId)
                        .child("Following").child(currentUserId)
                        .setValue(true)

                    //forwarding to home page
                    val intent=Intent(this@SignUpActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                }
                else
                {
                    val message=task.exception!!.toString()
                    Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}