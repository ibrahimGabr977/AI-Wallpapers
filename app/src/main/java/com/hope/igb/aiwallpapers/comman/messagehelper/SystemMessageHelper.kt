package com.hope.igb.aiwallpapers.comman.messagehelper

import android.content.Context
import android.widget.Toast

class SystemMessageHelper(private val context: Context) {


   fun showShortMessage(message: String){
       Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
   }

    fun showLongMessage(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}