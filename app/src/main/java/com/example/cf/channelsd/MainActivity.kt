package com.example.cf.channelsd

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val context = applicationContext
        sign_up_btn.setOnClickListener(){
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)

            Toast.makeText(context, "TEST", Toast.LENGTH_LONG).show();

        }
    }
}
