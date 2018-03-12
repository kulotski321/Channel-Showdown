package com.example.cf.channelsd.Activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Interfaces.LoginInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_login.*
import org.parceler.Parcels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var SESSION_KEY: String ?= null
    private var USERNAME : String ?= null
    private var EMAIL : String ?= null
    private var USERTYPE : String ?= null
    private var isMatch: Boolean = false
    private var loginInterface: LoginInterface ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginInterface = ApiUtils.apiLogin
        isMatch = false

        /*val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)

        val username1: String = preferences.getString(USERNAME,"NONE")
        val email: String = preferences.getString(EMAIL,"NONE")
        val userType: String = preferences.getString(USERTYPE,"NONE")
        val session_key: String = preferences.getString(SESSION_KEY,"NONE")
        toastMessage(username1)
*/
        sign_up_now_btn.setOnClickListener(){
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        sign_in_btn.setOnClickListener(){
            if(checkTextFields() == 2){
                val username = input_username_user.text.toString()
                val password = input_password_user.text.toString()
                sendPost(username,password)

                Log.d("isMatch:",isMatch.toString())
            }
        }
    }
    private fun popUpError(message: String, editText: EditText){
        editText.error = message
    }
    fun toastMessage(message: String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
    private fun editTextLength(editText: EditText): Int{
        return editText.text.toString().length
    }
    fun checkTextFields(): Int{
        var checked = 0
        if(editTextLength(input_username_user) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank",input_username_user)
        }
        if(editTextLength(input_password_user) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank",input_password_user)
        }
        return checked
    }
    fun sendPost(username: String,password: String) {
        loginInterface?.sendUserInfo(username,password)?.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
            }
            override fun onResponse(call: Call<User>?, response: Response<User>?) =
                    if (response!!.isSuccessful) {
                        val user = response.body()
                        SESSION_KEY = user?.session_key
                        USERNAME = user?.username
                        EMAIL = user?.email
                        USERTYPE = user?.userType
                        toastMessage(USERTYPE.toString())
                        /*val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = preferences.edit()
                        editor.putString(USERNAME,USERNAME)
                        editor.putString(SESSION_KEY,SESSION_KEY)
                        editor.putString(USERTYPE,USERTYPE)
                        editor.putString(EMAIL,EMAIL)
                        editor.apply()*/
                        val i = Intent(this@MainActivity,DashboardActivity::class.java)
                        val userInfo =  User(
                                SESSION_KEY.toString(),
                                USERNAME.toString(),
                                EMAIL.toString(),
                                "",
                                USERTYPE.toString()
                        )
                        i.putExtra("userinfo",Parcels.wrap(userInfo))
                        startActivity(i)
                        finish()
                    } else {
                        popUpError("invalid username or password", input_username_user)
                    }
        })
    }
}

