package com.example.cf.channelsd.Data

import com.example.cf.channelsd.Retrofit.RetrofitClient
import com.example.cf.channelsd.Interfaces.RegisterInterface
import com.example.cf.channelsd.Interfaces.LoginInterface
import com.example.cf.channelsd.Interfaces.LogoutInterface

object ApiUtils {

    val BASE_URL = "http://192.168.254.52:8000/"

    val apiRegister: RegisterInterface
        get() = RetrofitClient.getClient(BASE_URL)!!.create(RegisterInterface::class.java)
    val apiLogin: LoginInterface
        get() = RetrofitClient.getClient(BASE_URL)!!.create(LoginInterface::class.java)
    val apiLogout: LogoutInterface
        get() = RetrofitClient.getClient(BASE_URL)!!.create(LogoutInterface::class.java)
}