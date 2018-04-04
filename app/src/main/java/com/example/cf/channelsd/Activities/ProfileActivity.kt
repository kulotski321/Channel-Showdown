package com.example.cf.channelsd.Activities

//import com.example.cf.channelsd.Data.UserInfo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var user: User
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
                preferences.getString("profile_pic_pref", ""),
                preferences.getString("profile_vid_pref", ""),
                preferences.getString("profile_thumbnail_pref", "")

        )
        // set profile picture
        Log.e("profpic url:", ApiUtils.BASE_URL + user.profilePicture)
        picasso.load(ApiUtils.BASE_URL + user.profilePicture).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_picture)
        // set profile full name
        val fullName: String = user.firstName + " " + user.lastName
        // set profile video
        Log.e("thumbnail url:", ApiUtils.BASE_URL + user.profileThumbNail)
        picasso.load(ApiUtils.BASE_URL + user.profileThumbNail).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_video_thumbnail)

        if (user.firstName.isEmpty() || user.lastName.isEmpty()) {
            val default = "Full Name"
            profile_full_name.text = default
        } else {
            profile_full_name.text = fullName
        }
        profile_email.text = user.email
        if (user.bio.isEmpty()) {
            val default = "Describe yourself"
            profile_bio.text = default
        } else {
            profile_bio.text = user.bio
        }
        edit_profile_btn.setOnClickListener {
            val i = Intent(this, SelectEditActivity::class.java)
            startActivity(i)
        }
        profile_picture.setOnClickListener {
            val i = Intent(this, UploadPhotoActivity::class.java)
            startActivity(i)
        }
        profile_video_thumbnail.setOnClickListener {
            val videoUri: Uri = Uri.parse(ApiUtils.BASE_URL + user.profileVideo)
            val i = Intent(Intent.ACTION_VIEW, videoUri)
            i.setDataAndType(videoUri, "video/*")
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivity(i)
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this@ProfileActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, DashboardActivity::class.java)
        startActivity(i)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
    }
}

