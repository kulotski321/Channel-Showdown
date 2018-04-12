package com.example.cf.channelsd.Data

import com.example.cf.channelsd.Activities.LiveStreamCommentatorActivity

class Constants{
    class Perms{
        companion object {
            const val RC_VIDEO_APP_PERM = 124
            const val RC_SETTINGS_SCREEN_PERM = 123
            const val REQUEST_PERMISSION = 1
            val LOG_TAG = LiveStreamCommentatorActivity::class.java.simpleName!!
        }
    }
}