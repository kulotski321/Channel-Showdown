package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.cf.channelsd.R
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.cf.channelsd.Data.ApiUtils
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Key
import com.example.cf.channelsd.Data.Post
import com.example.cf.channelsd.Interfaces.APIService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/**
 * Created by CF on 3/5/2018.
 */
class TestActivity : AppCompatActivity() {

    private var mResponseTv: TextView? = null
    private var mAPIService: APIService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleEt = et_title
        val bodyEt = et_body
        val submitBtn = btn_submit
        mResponseTv = tv_response
        mAPIService = ApiUtils.apiService

        submitBtn.setOnClickListener {
            val title = titleEt.text.toString().trim()
            val body = bodyEt.text.toString().trim()
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                sendPost(title, body)
            }
        }
        btn_get_session.setOnClickListener {
            mAPIService?.getSessionID()?.enqueue(object : Callback<Key>{
                override fun onFailure(call: Call<Key>?, t: Throwable?) {
                    Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                }
                override fun onResponse(call: Call<Key>?, response: Response<Key>?) {
                    val session_id : Key = response?.body()!!
                    Log.e(ContentValues.TAG, session_id.session_id)
                    Toast.makeText(this@TestActivity, session_id.session_id?.trim(), Toast.LENGTH_LONG).show();
                }
            })
        }
    }
    fun sendPost(title: String, body: String) {
        mAPIService?.savePost(title, body, 1)?.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    showResponse(response.body().toString())
                    Log.i(ContentValues.TAG, "post submitted to API." + response.body().toString())
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.e(ContentValues.TAG, "Unable to submit post to API.")
            }
        })
    }
    fun showResponse(response: String) {
        if (mResponseTv?.visibility == View.GONE) {
            mResponseTv?.visibility = View.VISIBLE
        }
        mResponseTv?.text = response
    }
}