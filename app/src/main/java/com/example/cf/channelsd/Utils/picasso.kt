package com.example.cf.channelsd.Utils

import android.content.Context
import com.squareup.picasso.Picasso

val Context.picasso: Picasso
    get() = Picasso.Builder(this).build()