package com.example.cf.channelsd.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.cf.channelsd.Data.Result
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity: AppCompatActivity() {
    private var eventInterface: EventInterface = ApiUtils.apiEvent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val eventId : String = intent.getStringExtra("event_id")
        viewResult(eventId.toInt())

    }
    private fun viewResult(eventId : Int){
        eventInterface.viewResult(eventId).enqueue(object: Callback<Result>{
            override fun onFailure(call: Call<Result>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    viewResult(eventId)
                }
            }

            override fun onResponse(call: Call<Result>?, response: Response<Result>?) {
                val c1 : CircleImageView = findViewById(R.id.contestant1_image)
                val c2 : CircleImageView = findViewById(R.id.contestant2_image)
                if(response!!.isSuccessful){
                    val result = response.body()
                    Log.e("result:",result.toString())
                    if (result != null){
                        picasso.load(ApiUtils.BASE_URL + result.contestant1Image).into(c1)
                        picasso.load(ApiUtils.BASE_URL + result.contestant2Image).into(c2)
                        contestant1Name.text = result.contestant1
                        contestant2Name.text = result.contestant2
                        val total : Float = (result.votes1 + result.votes2).toFloat()
                        Log.e("votes1:",result.votes1.toString())
                        Log.e("votes2:",result.votes2.toString())
                        Log.e("total:",total.toString())
                        val progressVotes1 : Float = (result.votes1/total) * 100
                        val progressVotes2 : Float = (result.votes2/total) * 100
                        Log.e("votes1:",progressVotes1.toString())
                        Log.e("votes2:",progressVotes2.toString())
                        votes1.scaleY = 3F
                        votes2.scaleY = 3F
                        votes1.progress = progressVotes1.toInt()
                        votes2.progress = progressVotes2.toInt()
                        contestant1_image.setOnClickListener {
                            val i = Intent(this@ResultActivity,ViewProfileActivity::class.java)
                            i.putExtra("from_link","true")
                            i.putExtra("entry_id","blank")
                            i.putExtra("username",contestant1Name.text.toString())
                            startActivity(i)
                        }
                        contestant2_image.setOnClickListener {
                            val i = Intent(this@ResultActivity,ViewProfileActivity::class.java)
                            i.putExtra("from_link","true")
                            i.putExtra("entry_id","blank")
                            i.putExtra("username",contestant2Name.text.toString())
                            startActivity(i)
                        }
                    }
                }
            }
        })
    }
}