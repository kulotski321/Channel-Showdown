package com.example.cf.channelsd.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.cf.channelsd.Data.User
//import com.example.cf.channelsd.Data.UserInfo
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_profile.*
import org.parceler.Parcels

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
                preferences.getString("bio_pref", "")
        )
        val fullName: String = user?.firstName + " " + user?.lastName
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