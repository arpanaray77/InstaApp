package com.example.instaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.instaapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_comment.*
import kotlinx.android.synthetic.main.activity_add_post.*

class AddCommentActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>( R.id.comments_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Comments"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        firebaseUser= FirebaseAuth.getInstance().currentUser

         val add_comment=findViewById<EditText>(R.id.add_comment)
         val post=findViewById<TextView>(R.id.post_comment)

        val postid = intent.getStringExtra("POST_ID")


        post.setOnClickListener {
            if(add_comment.text.toString().equals(""))
            {
              Toast.makeText(this,"You can't send an empty comment",Toast.LENGTH_SHORT).show()
            }
            else
            {
                postComment(postid!!)
            }
        }
        getImage()
    }

    private fun postComment(postid:String) {

        val commentRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Comments").child(postid)

        val commentMap = HashMap<String, Any>()
        commentMap["comment"] = add_comment.text.toString()
        commentMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

        commentRef.push().setValue(commentMap)
        add_comment.setText("")
        Toast.makeText(this, "posted!!", Toast.LENGTH_LONG).show()
    }

    private fun getImage() {
        val ref : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(user_profile_image)
                }
            }
        })
    }
}