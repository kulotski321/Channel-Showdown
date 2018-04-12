package com.example.cf.channelsd.Activities

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Reply
import com.example.cf.channelsd.Interfaces.ProfileInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Retrofit.ProgressRequestBody
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.ImageFilePath
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_upload_thumbnail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadThumbnailActivity : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {

    private var progressBar: ProgressBar? = null
    private val profileInterface: ProfileInterface = ApiUtils.apiProfile
    private val RESULT_LOAD_IMAGE = 1
    private lateinit var filePartImage: RequestBody
    private var fileImage: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_thumbnail)

        progressBar = progressBarThumbnail
        progressBar!!.visibility = View.INVISIBLE
        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        val username: String = preferences.getString("username_pref", "")
        val profileThumbNail: String = preferences.getString("profile_thumbnail_pref", "")
        picasso.load(ApiUtils.BASE_URL + profileThumbNail).resize(75,75).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_video_upload_thumbnail)

        select_thumbnail_btn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
        }
        upload_video_thumbnail_btn.setOnClickListener {
            if (fileImage != null) {
                progressBar!!.visibility = View.VISIBLE
                val usernameRB: RequestBody = RequestBody.create(MediaType.parse("text/plain"), username)
                uploadThumbnail(usernameRB, fileImage!!)
                //toastMessage("Upload successful")
                //finish()
            } else {
                toastMessage("Please select thumbnail")
            }
        }
        cancel_thumbnail_upload_video_btn.setOnClickListener {
            finish()
        }
        select_thumbnail_btn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
        }
    }

    private fun uploadThumbnail(username: RequestBody, image: MultipartBody.Part) {
        profileInterface.uploadThumbNail(username, image).enqueue(object : Callback<Reply> {
            override fun onFailure(call: Call<Reply>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of stream") {
                    uploadThumbnail(username, image)
                } else {
                    toastMessage("Check your internet connection")
                    progressBar!!.visibility = View.INVISIBLE
                    progressBar!!.progress = 0
                }
            }

            override fun onResponse(call: Call<Reply>?, response: Response<Reply>?) {
                if (response!!.isSuccessful) {
                    val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    val replyResponse = response.body()
                    val thumbnail = replyResponse?.thumbNail
                    Log.e("profile response", replyResponse.toString())
                    editor.putString("profile_thumbnail_pref", thumbnail.toString())
                    editor.apply()
                    finish()
                    toastMessage("Thumbnail Uploaded")
                } else {
                    toastMessage("Upload failed")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri = data.data

            val realPath = ImageFilePath.getPath(this@UploadThumbnailActivity, data.data)
            val originalFile = File(realPath)

            Log.e("image path:", realPath)

            profile_video_upload_thumbnail.setImageURI(selectedImage) // Set thumbnail picture

            filePartImage = ProgressRequestBody(this, originalFile, this, selectedImage)
            fileImage = MultipartBody.Part.createFormData("image", originalFile.name, filePartImage)
        }
    }

    private fun toastMessage(message: String) {
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val toastView: View = toast.view
        val toastMessage: TextView = toastView.findViewById(android.R.id.message)
        toastMessage.textSize = 20F
        toastMessage.setPadding(4, 4, 4, 4)
        toastMessage.setTextColor(Color.parseColor("#790e8b"))
        toastMessage.gravity = Gravity.CENTER
        toastView.setBackgroundColor(Color.YELLOW)
        toastView.setBackgroundResource(R.drawable.round_button1)
        toast.show()
    }

    override fun onProgressUpdate(percentage: Long) {
        progressBar!!.progress = percentage.toInt()
        Log.e("percentage:", percentage.toString())
    }

    override fun onError() {
        toastMessage("Upload failed")
    }

    override fun onFinish() {
        progressBar!!.progress = 100
    }

}