package com.example.objecttensor

import android.graphics.Bitmap

interface Classifier {

    fun recognizeImage(bitmap: Bitmap?): List<Recognition?>?

    fun enableStatLogging(debug: Boolean)

    fun getStatString(): String?

    fun close()

    fun setNumThreads(num_threads: Int)

    fun setUseNNAPI(isChecked: Boolean)
}