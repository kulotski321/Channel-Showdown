package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Intent
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var loginInterface: LoginInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginInterface = ApiUtils.apiLogin
        sign_up_now_btn.setOnClickListener(){
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        sign_in_btn.setOnClickListener(){
            /*if(checkTextFields() == 2){
                val username = input_username_user.toString().trim()
                val password = input_password_user.toString().trim()
                sendPost(username,"",password,"")
            }*/
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }
    fun popUpError(message: String,editText: EditText){
        editText.error = message
    }
    fun toastMessage(message: String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show();
    }
    fun editTextLength(editText: EditText): Int{
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
    fun sendPost(username: String,email: String,password: String,userType: String){
        loginInterface?.sendUserInfo(username,email,password,userType)?.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
            }
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if(response!!.isSuccessful){

                }
            }
        })
    }
}

