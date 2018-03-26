package com.example.cf.channelsd.Activities

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Reply
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Interfaces.ProfileInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.picasso
import kotlinx.android.synthetic.main.activity_additional_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import com.example.cf.channelsd.Utils.ImageFilePath


class InfoActivity : AppCompatActivity() {

    private val profileInterface: ProfileInterface = ApiUtils.apiProfile
    private var user: User? = null
    private val RESULT_LOAD_IMAGE = 1
    private val RESULT_LOAD_VIDEO = 1
    private lateinit var filePartImage: RequestBody
    private var fileImage : MultipartBody.Part ?= null
    private lateinit var filePartVideo: RequestBody
    private var fileVideo : MultipartBody.Part ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_additional_info)
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
        val firstNameInput: EditText = input_first_name
        val lastNameInput: EditText = input_last_name
        val bioInput: EditText = input_bio

        firstNameInput.setText(user?.firstName, TextView.BufferType.EDITABLE)
        lastNameInput.setText(user?.lastName, TextView.BufferType.EDITABLE)
        bioInput.setText(user?.bio, TextView.BufferType.EDITABLE)
        picasso.load(ApiUtils.BASE_URL+ user!!.profilePicture).into(upload_profile_pic)
        confirm_btn.setOnClickListener {
            if (checkTextFields() == 3) {
                val username: String = preferences.getString("username_pref", "")
                val firstName: String = input_first_name.text.toString()
                val lastName: String = input_last_name.text.toString()
                val bio: String = input_bio.text.toString()
                sendPost(username, firstName, lastName, bio)
                if(fileImage != null){
                    //Log.e("path",filePart.toString())
                    val usernameRB : RequestBody = RequestBody.create(MediaType.parse("text/plain"),username)
                    uploadPhoto(usernameRB,fileImage!!)
                }
                if(fileVideo != null){
                    //Log.e("path",filePart.toString())
                    val usernameRB : RequestBody = RequestBody.create(MediaType.parse("text/plain"),username)
                    uploadVideo(usernameRB,fileVideo!!)
                }
                val i = Intent(this, ProfileActivity::class.java)
                startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
            }
        }
        upload_pic_btn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE)
        }
        upload_video_btn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "video/*"
            startActivityForResult(galleryIntent,RESULT_LOAD_VIDEO)
        }
    }

    private fun editTextLength(editText: EditText): Int {
        return editText.text.toString().length
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this@InfoActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun checkTextFields(): Int {
        var checked = 0
        if (editTextLength(input_first_name) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_first_name)
        }
        if (editTextLength(input_last_name) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_last_name)
        }
        if (editTextLength(input_bio) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_bio)
        }
        return checked
    }

    private fun popUpError(message: String, editText: EditText) {
        editText.error = message
    }

    private fun sendPost(username: String, firstName: String, lastName: String, bio: String) {
        profileInterface?.sendAdditionalInfo(username, firstName, lastName, bio)?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of steam") {
                    sendPost(username, firstName, lastName, bio)
                }
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response!!.isSuccessful) {
                    toastMessage("Profile updated")
                    val userInfo = response.body()
                    val firstNameNew = userInfo?.firstName
                    val lastNameNew = userInfo?.lastName
                    val bioNew = userInfo?.bio
                    val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    editor.putString("firstName_pref", firstNameNew)
                    editor.putString("lastName_pref", lastNameNew)
                    editor.putString("bio_pref", bioNew)
                    editor.apply()
                    val i = Intent(this@InfoActivity, ProfileActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
            }
        })
    }
    private fun uploadPhoto(username: RequestBody,image: MultipartBody.Part){
        profileInterface?.uploadPhoto(username,image)?.enqueue(object: Callback<Reply>{
            override fun onFailure(call: Call<Reply>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of steam") {
                    uploadPhoto(username,image)
                }
            }

            override fun onResponse(call: Call<Reply>?, response: Response<Reply>?) {
                if(response!!.isSuccessful){
                    val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    val replyRespone = response.body()
                    val profilePic = replyRespone?.profilePic
                    Log.e("profile response",replyRespone.toString())
                    editor.putString("profile_pic_pref",profilePic.toString())
                    editor.apply()
                }
            }
        })
    }
    private fun uploadVideo(username:RequestBody,video: MultipartBody.Part){
        profileInterface?.uploadVideo(username,video)?.enqueue(object: Callback<Reply>{
            override fun onFailure(call: Call<Reply>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of steam") {
                    uploadVideo(username,video)
                }
            }

            override fun onResponse(call: Call<Reply>?, response: Response<Reply>?) {
                if(response!!.isSuccessful){
                    val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = preferences.edit()
                    val replyRespone = response.body()
                    val profileVideo = replyRespone?.profileVideo
                    Log.e("profile response",replyRespone.toString())
                    editor.putString("profile_video_pref",profileVideo.toString())
                    editor.apply()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, ProfileActivity::class.java)
        startActivity(i)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            val selectedImage: Uri = data.data

            val realPath = ImageFilePath.getPath(this@InfoActivity,data.data)
            val originalFile = File(realPath)

            Log.e("image path:",realPath)

            upload_profile_pic.setImageURI(selectedImage) // Set

            filePartImage = RequestBody.create(
                    MediaType.parse(contentResolver.getType(selectedImage)),originalFile)
            fileImage = MultipartBody.Part.createFormData("image",originalFile.name,filePartImage)
        }
        if(requestCode == RESULT_LOAD_VIDEO && resultCode == Activity.RESULT_OK && data != null){
            val selectedVideo: Uri = data.data

            val realPath = ImageFilePath.getPath(this@InfoActivity,data.data)
            val originalFile = File(realPath)

            Log.e("video path:",realPath)

            upload_profile_pic.setImageURI(selectedVideo)
            filePartVideo = RequestBody.create(
                    MediaType.parse(contentResolver.getType(selectedVideo)),originalFile)
            fileVideo = MultipartBody.Part.createFormData("video",originalFile.name,filePartVideo)
        }
    }
}