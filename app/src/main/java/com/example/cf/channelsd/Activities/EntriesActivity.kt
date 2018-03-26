package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.cf.channelsd.Adapters.EntriesAdapter
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Data.EntryList
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_entries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntriesActivity: AppCompatActivity() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        val eventId : String = intent.getStringExtra("eventId")
        getEntries(eventId.toInt())
    }
    private fun getEntries(eventId: Int){
        eventInterface.getEntries(eventId).enqueue(object : Callback<EntryList> {
            override fun onFailure(call: Call<EntryList>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if(t?.message == "unexpected end of stream"){
                    getEntries(eventId)
                }
            }

            override fun onResponse(call: Call<EntryList>?, response: Response<EntryList>?) {
                if(response!!.isSuccessful){
                    val entryList : EntryList? = response.body()
                    if(entryList != null){
                        val entries = entryList.entries
                        val entryRecyclerviwer = entries_RV
                        entryRecyclerviwer.layoutManager = LinearLayoutManager(applicationContext,LinearLayout.VERTICAL,false)
                        val adapter = EntriesAdapter(entries!!)
                        entryRecyclerviwer.adapter = adapter
                    }
                }
            }
        })
    }
    fun toastMessage(message: String){
        Toast.makeText(this@EntriesActivity, message, Toast.LENGTH_LONG).show();
    }

    override fun onResume() {
        super.onResume()
        val eventId : String = intent.getStringExtra("eventId")
        getEntries(eventId.toInt())
    }
}