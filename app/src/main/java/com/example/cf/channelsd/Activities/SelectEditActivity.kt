package com.example.cf.channelsd.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_select_edit.*
import org.parceler.Parcels

class SelectEditActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_edit)

        edit_personal_information_btn.setOnClickListener {
            val i = Intent(this, InfoActivity::class.java)
            startActivity(i)
        }
        upload_profile_video_btn.setOnClickListener {
            val i = Intent(this, UploadVideoActivity::class.java)
            startActivity(i)
        }
        upload_thumbnail_btn.setOnClickListener {
            val i = Intent(this, UploadThumbnailActivity::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, ProfileActivity::class.java)
        startActivity(i)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
    }
}