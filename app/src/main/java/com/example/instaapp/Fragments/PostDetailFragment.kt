package com.example.instaapp.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.Adapter.MyPostAdapter
import com.example.instaapp.Adapter.PostAdapter
import com.example.instaapp.Model.Post
import com.example.instaapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostDetailFragment : Fragment() {

    private var postAdapter: PostAdapter?=null
    private var postList:MutableList<Post>?=null
    private var postid:String?=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_post_detail, container, false)

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid=pref?.getString("postid","none")

        var recyclerView:RecyclerView?=null
        recyclerView=view.findViewById(R.id.recyclerview_postdetail)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager=linearLayoutManager

        postList=ArrayList()
        postAdapter=context?.let { PostAdapter(it,postList as ArrayList<Post>) }
        recyclerView.adapter=postAdapter

        readPosts()

        return view
    }

    private fun readPosts() {
        val postRef= FirebaseDatabase.getInstance().reference.child("Posts").child(postid.toString())

        postRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot)
            {
                    postList?.clear()
                    for (snapshot in p0.children)
                    {
                        val post = snapshot.getValue(Post::class.java)
                        if (post != null) {
                            postList!!.add(post)
                        }
                         postAdapter!!.notifyDataSetChanged()
                    }
                }
            })
        }
}