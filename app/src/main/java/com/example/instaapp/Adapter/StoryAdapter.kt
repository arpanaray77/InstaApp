package com.example.instaapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.MainActivity
import com.example.instaapp.Model.Post
import com.example.instaapp.Model.Story
import com.example.instaapp.Model.User
import com.example.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.view.*

class StoryAdapter(private val mContext: Context, private  val mStory:List<Story>):RecyclerView.Adapter<StoryAdapter.ViewHolder>(){

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

        var story_username: TextView = itemView.findViewById(R.id.story_username)
        var story_text: TextView = itemView.findViewById(R.id.story_text)
        var story_img_seen: ImageView = itemView.findViewById(R.id.story_photo_seen)
        var story_plus: CircleImageView = itemView.findViewById(R.id.story_plus)
        var story_img: ImageView = itemView.findViewById(R.id.story_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        if (i == 0) {
            val view= LayoutInflater.from(mContext).inflate(R.layout.add_story_layout,parent,false)
            return StoryAdapter.ViewHolder(view)
        }
        else
        {
            val view= LayoutInflater.from(mContext).inflate(R.layout.story_item_layout,parent,false)
            return StoryAdapter.ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
       return mStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = mStory[position]

        getUserInfo(holder,story.getStoryid(),position)

        if(holder.adapterPosition!=0)
        {
            seenStory(holder,story.getUserid())
        }

        if(holder.adapterPosition==0)
        {
            myStory(holder.story_text,holder.story_plus,true)
        }
        else
        {

        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0)
            return 0
        else
            return 1
    }

    private fun getUserInfo(viewHolder: ViewHolder,userid:String,pos:Int) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(userid)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).into(viewHolder.story_img_seen)
                    if(pos!=0)
                    {
                        Picasso.get().load(user.getImage()).into(viewHolder.story_img)
                        viewHolder.story_username.text=user.getUsername()
                    }
                }
            }
        })
    }

    private fun myStory (textView: TextView,imageView: ImageView,click:Boolean) {

        val Ref = FirebaseDatabase.getInstance().reference.child("Story").child( FirebaseAuth.getInstance().currentUser!!.uid)
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var count=0
                val timecurrent=System.currentTimeMillis()
                for(snapshot in p0.children)
                {
                    val story=snapshot.getValue(Story::class.java)
                    if(timecurrent> story!!.getTimestart() && timecurrent< story.getTimeend())
                    {
                        count+=1
                    }
                }
                if(click)
                {

                }
                else
                {
                    if(count>0) {
                        textView.text = "My Story"
                        imageView.visibility = View.GONE
                    }
                    else
                    {
                        textView.text="Add a Story"
                        imageView.visibility=View.VISIBLE
                    }
                }
            }
        })
    }

    private fun seenStory (viewHolder: ViewHolder,userid: String) {

        val Ref = FirebaseDatabase.getInstance().reference.child("Story").child( userid)
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var i=0
                for(snapshot in p0.children)
                {
                    val story=snapshot.getValue(Story::class.java)
                    if(!snapshot.child("views").child(FirebaseAuth.getInstance().currentUser!!.uid).exists() &&
                                System.currentTimeMillis()< story!!.getTimeend())
                        {
                            i += 1
                        }
                }
                    if(i>0) {
                        viewHolder.story_img.visibility=View.VISIBLE
                        viewHolder.story_img_seen.visibility=View.GONE
                    }
                    else
                    {
                        viewHolder.story_img.visibility=View.GONE
                        viewHolder.story_img_seen.visibility=View.VISIBLE
                    }
              }
        })
    }

}