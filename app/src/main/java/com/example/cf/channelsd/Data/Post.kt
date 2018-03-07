package com.example.cf.channelsd.Data

/**
 * Created by CF on 3/5/2018.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Post {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("body")
    @Expose
    var body: String? = null
    @SerializedName("userId")
    @Expose
    var userId: Int? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null

    override fun toString(): String {
        return "Post{" +
                "title='" + title + '\''.toString() +
                ", body='" + body + '\''.toString() +
                ", userId=" + userId +
                ", id=" + id +
                '}'.toString()
    }
}