package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Interfaces.RegisterInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var registerInterface: RegisterInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerInterface = ApiUtils.apiRegister
        sign_up_btn.setOnClickListener {
            if (checkTextFields() == 3) {
                val username: String = input_username_register.text.toString()
                val email: String = input_email_register.text.toString()
                val password1: String = input_password_register.text.toString()
                val password2 = password1
                val userType: String = if (input_commentator_Btn.isChecked) {
                    "commentator"
                } else {
                    "normal"
                }
                if (editTextLength(input_password_register) < 8) {
                    popUpError("password must be at least 8 characters", input_password_register)
                } else {
                    sendPost(username, email, password1, password2, userType)
                }
            }
        }
    }

    private fun editTextLength(editText: EditText): Int {
        return editText.text.toString().length
    }

    private fun popUpError(message: String, editText: EditText) {
        editText.error = message
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

    private fun sendPost(username: String, email: String, password1: String, password2: String, userType: String) {
        registerInterface?.createUserInfo(username, email, password1, password2, userType)?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of stream") {
                    sendPost(username, email, password1, password2, userType)
                }
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                //Log.e("response errorbody", response!!.errorBody()?.string())

                if (response!!.isSuccessful) {
                    val user = response.body()
                    Log.e("response body", user.toString())
                    toastMessage("Register successful")
                    val i = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    toastMessage("Register failed")
                }
            }
        })
    }

    private fun checkTextFields(): Int {
        var checked = 0
        if (editTextLength(input_username_register) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_username_register)
        }
        if (editTextLength(input_email_register) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_email_register)
        }
        if (editTextLength(input_password_register) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_password_register)
        }
        return checked
    }
}
