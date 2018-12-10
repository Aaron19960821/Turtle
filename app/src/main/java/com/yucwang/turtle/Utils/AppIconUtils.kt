package com.yucwang.turtle.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AppIconUtils() {
    companion object {

        val TAG = "AppIconUtils"

        /**
         * Get the bitmap of a drawable
         */
        fun cvtDrawableToBitmap(drawable : Drawable) : Bitmap {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.bounds = Rect(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        }

        /**
         * Save a bitmap to a given file
         */
        fun saveBitMapToFile(context: Context, bitmap : Bitmap, fileName : String,
                             format : Bitmap.CompressFormat, quality : Int) {
            val imageFile = File(context.filesDir, fileName)
            var imageFileOutputStream : FileOutputStream? = null
            try {
                imageFileOutputStream = FileOutputStream(imageFile)
                bitmap.compress(format, quality, imageFileOutputStream)
                imageFileOutputStream.close()
            } catch (e : IOException) {
                if (imageFileOutputStream == null) {
                    imageFileOutputStream!!.close()
                }
            }
        }

        fun renameAppIconFile(context: Context, oldFileName: String, newFileName: String) {
            try {
                val oldFile = File(context.filesDir, oldFileName)
                val newFile = File(context.filesDir, newFileName)
                oldFile.renameTo(newFile)
            } catch(e : Exception) {
                Log.d(TAG, "can not rename the file")
            }
        }
    }
}