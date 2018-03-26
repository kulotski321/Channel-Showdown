package com.example.cf.channelsd.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.example.cf.channelsd.Data.User
//import com.example.cf.channelsd.Data.UserInfo
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_profile.*
import org.parceler.Parcels
import java.io.File
import java.io.InputStream
import java.net.URL
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.widget.MediaController
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.Picasso
import org.jetbrains.anko.ctx
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.cf.channelsd.Utils.ImageUtil
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity() {

    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        user = User(
                preferences.getString("session_key_pref", ""),
                preferences.getString("username_pref", ""),
                preferences.getString("email_pref", ""),
                preferences.getString("userType_pref", ""),
                preferences.getString("firstName_pref", ""),
                preferences.getString("lastName_pref", ""),
                preferences.getString("bio_pref", ""),
                preferences.getString("profile_pic_pref",""),
                preferences.getString("profile_vid_pref","")
        )
        // set profile picture
        picasso.load(ApiUtils.BASE_URL+ user!!.profilePicture).into(profile_picture)
        // set profile full name
        val fullName: String = user?.firstName + " " + user?.lastName
        // set profile video

        /*if(preferences.getString("thumbnail_pref","").isNullOrEmpty()){
            toastMessage("thumbnail is null or empty")
            val baos : ByteArrayOutputStream ?= null
            val thumbNailVideo : Bitmap = ImageUtil.retriveVideoFrameFromVideo(ApiUtils.BASE_URL+user!!.profileVideo)!!
            thumbNailVideo.compress(Bitmap.CompressFormat.PNG,100,baos)
            val b = baos!!.toByteArray()
            val encoded: String = Base64.encodeToString(b,Base64.DEFAULT)
            Log.e("ENCODED:",encoded)
            editor.putString("thumbnail_pref",encoded)
            editor.apply()
        }else{
            toastMessage("thumbnail is set")
            val encoded: String = preferences.getString("thumbnail_pref","")
            val imageAsBytes : ByteArray = Base64.decode(encoded,Base64.DEFAULT)
            profile_video_thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0,imageAsBytes.size))
        }*/

        val videoUri: Uri = Uri.parse(ApiUtils.BASE_URL+user!!.profileVideo)
        // partially working
        /*val thumbNailVideo : Bitmap = ImageUtil.retriveVideoFrameFromVideo(ApiUtils.BASE_URL+user!!.profileVideo)!!
        profile_video_thumbnail.setImageBitmap(thumbNailVideo)*/

        profile_video_thumbnail.setOnClickListener {
            val playVideoIntent = Intent(Intent.ACTION_VIEW)
            playVideoIntent.setDataAndType(videoUri,"video/*")
            playVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //playVideoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivity(playVideoIntent)
        }

        if (user?.firstName.isNullOrEmpty() || user?.lastName.isNullOrEmpty()) {
            val default = "Full Name"
            profile_full_name.text = default
        } else {
            profile_full_name.text = fullName
        }
        profile_email.text = user?.email
        if (user?.bio.isNullOrEmpty()) {
            val default = "Describe yourself"
            profile_bio.text = default
        } else {
            profile_bio.text = user?.bio
        }
        edit_profile_btn.setOnClickListener {
            val i = Intent(this, InfoActivity::class.java)
            i.putExtra("user", Parcels.wrap(user))
            startActivity(i)
            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
        }
    }
    private fun toastMessage(message: String) {
        Toast.makeText(this@ProfileActivity,message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, DashboardActivity::class.java)
        i.putExtra("user", Parcels.wrap(user))
        startActivity(i)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
    }
}

