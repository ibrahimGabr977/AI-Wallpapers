package com.hope.igb.aiwallpapers.util

import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class MuteSounds {


    companion object{

         fun muteAds(context: Context?){
            val  audioManager = context!!.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
//                savedStreamMuted = true;
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
                }
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
            }
        }
    }

}