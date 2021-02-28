package com.example.instaapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaapp.Adapter.PostAdapter
import com.example.instaapp.Model.Post
import com.example.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var postAdapter:PostAdapter?=null
    private var postList:MutableList<Post>?=null
    private var followingList:MutableList<Post>?=null


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)

        var recyclerView:RecyclerView?=null
        recyclerView=view.findViewById(R.id.recyclerview_home)
        val linearLayoutManager:LinearLayoutManager=LinearLayoutManager(context)
        linearLayoutManager.reverseLayout=true
        linearLayoutManager.stackFromEnd=true
        recyclerView.layoutManager=linearLayoutManager


         postList=ArrayList()
         postAdapter=context?.let { PostAdapter(it,postList as ArrayList<Post>) }
         recyclerView.adapter=postAdapter

         //code for counting no of items in recycler view
//         if (postAdapter!!.itemCount == 0){
//             welcome_text.text = "Welcome to Instagram"
//         }
//         else
//         {
//             welcome_text.visibility=View.INVISIBLE
//         }

         checkFollowings()

        return view
    }

    //to get the following List of logged-in user
    private fun checkFollowings() {
        followingList=ArrayList()

        val followingRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")

        followingRef.addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    (followingList as ArrayList<String>).clear() //to get previous data
                    for(snapshot in p0.children)
                    {
                        snapshot.key?.let { (followingList as ArrayList<String>).add(it) }
                    }
                    retrievePosts()
                }
            }
        })
    }

    private fun retrievePosts() {
        val postRef=FirebaseDatabase.getInstance().reference.child("Posts")

         postRef.addValueEventListener(object : ValueEventListener
         {
             override fun onCancelled(error: DatabaseError) {

             }

             override fun onDataChange(p0: DataSnapshot)
             {
                 if(p0.exists()) {
                     postList?.clear()
                     for (snapshot in p0.children) {
                         val post = snapshot.getValue(Post::class.java)

                         for (id in (followingList as ArrayList<String>)) {
                             if (post!!.getPublisher() == id) {
                                 postList!!.add(post)
                             }
                             postAdapter!!.notifyDataSetChanged()
                         }
                     }
                 }
             }

         })
    }
}
