package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Interfaces.APIService
import com.example.cf.channelsd.Interfaces.RegisterInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CF on 3/1/2018.
 */
class RegisterActivity : AppCompatActivity() {

    private var mAPIService: APIService? = null
    private var registerInterface: RegisterInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAPIService = ApiUtils.apiService
        registerInterface = ApiUtils.apiRegister
        sign_up_btn.setOnClickListener {
            if(checkTextFields() == 3){
                val fullname = input_fullname_register.toString().trim()
                val email = input_email_register.toString().trim()
                val password = input_password_register.toString().trim()
                val userType: String = if(input_commentator_Btn.isChecked){
                    "Commentator"
                }else{
                    "Simple"
                }
                sendPost(fullname,email,password,userType)
            }
        }
    }
    fun editTextLength(editText: EditText): Int{
        return editText.text.toString().length
    }
    fun popUpError(message: String,editText: EditText){
        editText.error = message
    }
    fun toastMessage(message: String){
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show();
    }
    fun sendPost(fullname: String, email: String, password: String,userType: String){
        registerInterface?.createUserInfo(fullname,email,password,userType)?.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
            }
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if(response!!.isSuccessful){
                    toastMessage("Register successful")
                }
            }
        })
    }
    fun checkTextFields(): Int{
        var checked = 0
        if(editTextLength(input_fullname_register) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank",input_fullname_register)
        }
        if(editTextLength(input_email_register) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank",input_email_register)
        }
        if(editTextLength(input_password_register) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank",input_password_register)
        }
        return checked
    }
}
