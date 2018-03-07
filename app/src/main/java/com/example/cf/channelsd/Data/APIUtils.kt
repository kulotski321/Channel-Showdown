package com.example.cf.channelsd.Data

import com.example.cf.channelsd.Interfaces.APIService
import com.example.cf.channelsd.Retrofit.RetrofitClient
import com.example.cf.channelsd.Interfaces.RegisterInterface
import com.example.cf.channelsd.Interfaces.LoginInterface
/**
 * Created by CF on 3/5/2018.
 */
object ApiUtils {
    //val BASE_URL = "http://jsonplaceholder.typicode.com/"
    val BASE_URL = "http://192.168.254.52:8000"
    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(APIService::class.java)
    val apiRegister: RegisterInterface
        get() = RetrofitClient.getClient(BASE_URL)!!.create(RegisterInterface::class.java)
    val apiLogin: LoginInterface
        get() = RetrofitClient.getClient(BASE_URL)!!.create(LoginInterface::class.java)
}