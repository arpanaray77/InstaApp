package com.example.instaapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddPostActivity : AppCompatActivity() {

    private  var myUrl=""
    private  var imageUri: Uri?=null
    private  var storagePostPictureRef: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        storagePostPictureRef= FirebaseStorage.getInstance().reference.child("Post Picture")
    }
}