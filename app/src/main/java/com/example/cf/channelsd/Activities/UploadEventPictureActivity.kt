package com.example.cf.channelsd.Activities

import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.Utils.ApiUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadEventPictureActivity : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private val RESULT_LOAD_IMAGE = 1
    private lateinit var filePartImage: RequestBody
    private var fileImage : MultipartBody.Part ?= null


}