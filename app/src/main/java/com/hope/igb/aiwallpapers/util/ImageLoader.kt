package com.hope.igb.aiwallpapers.util



import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.hope.igb.aiwallpapers.R


class ImageLoader(private val context: Context){



    private fun customProgress(): CircularProgressDrawable {

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 4f
        circularProgressDrawable.centerRadius = 35f
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(context, R.color.blue_color))
        circularProgressDrawable.start()

        return circularProgressDrawable
    }


    fun loadImage(image: String ): RequestBuilder<Drawable> {
        return Glide.with(context)
            .load(image)
            .placeholder(customProgress())
            .error(R.drawable.wallpaper_error)


    }





}


