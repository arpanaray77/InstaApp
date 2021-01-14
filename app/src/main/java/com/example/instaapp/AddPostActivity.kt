package com.example.instaapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {

    private  var myUrl=""
    private  var imageUri: Uri?=null
    private  var storagePostPictureRef: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        storagePostPictureRef= FirebaseStorage.getInstance().reference.child("Post Picture")

        post_picture.setOnClickListener {
            uploadImage()
        }
        CropImage.activity()
            .setAspectRatio(1,1)
            .start(this@AddPostActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== Activity.RESULT_OK && data!=null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image_profile.setImageURI(imageUri)
        }

    }

    private fun uploadImage() {
        TODO("Not yet implemented")
    }
}