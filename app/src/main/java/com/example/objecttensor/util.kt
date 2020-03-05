package com.example.objecttensor

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


fun ByteArray.convertBitmap() : Bitmap {

    return BitmapFactory.decodeByteArray(this, 0, this.size)
}