package com.aijia.framework.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter


object Utils {
    fun makeTintBitmap(inputBitmap: Bitmap?, tintColor: Int): Bitmap? {
        if (inputBitmap == null) {
            return null
        }
        val outputBitmap =
            Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(inputBitmap, 0f, 0f, paint)
        return outputBitmap
    }
}