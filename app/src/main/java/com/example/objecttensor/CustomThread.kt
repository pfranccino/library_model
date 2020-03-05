package com.example.objecttensor

import android.graphics.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.util.concurrent.LinkedBlockingQueue

class CustomThread(private val queue: LinkedBlockingQueue<YuvImage>, val detector: Classifier , private val listener: TensorFlowListener) :
    Thread() {

    var start = false
    var detection = true
    var croppedBmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun run() {
        super.run()
        customRun(queue)

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun customRun(queue: LinkedBlockingQueue<YuvImage>) {

        while (detection) {
            if (queue.size == 1) {
                start = true
            }
            if (start) {
                val inputFrame = queue.poll()
                if (inputFrame != null) {
                    val bitmapFrame = rotateBitmap(
                        inputFrame, 90,
                        Rect(0, 0, inputFrame.width, inputFrame.height)
                    )

                 runObjectDetection(bitmapFrame!!)


                }

            }
        }
    }

    fun runObjectDetection(srcBmp: Bitmap): Bitmap {
        croppedBmp = Bitmap.createBitmap(
            srcBmp,
            0,
            0,
            srcBmp.width,
            (srcBmp.height) - 2 * 0
        )
        val scaledBmp = Bitmap.createScaledBitmap(
            croppedBmp!!,
            300,
            300,
            true
        )

        val results = detector.recognizeImage(scaledBmp)
        Log.e(javaClass.simpleName , results.toString())

        listener.OnSuccessListener(results)


        return scaledBmp
    }

    private fun rotateBitmap(yuvImage: YuvImage, orientation: Int, rectangle: Rect): Bitmap? {
        val os = ByteArrayOutputStream()
        yuvImage.compressToJpeg(rectangle, 100, os)
        val matrix = Matrix()
        matrix.postRotate(orientation.toFloat())
        val bytes = os.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun start() {
        super.start()
        queue.clear()
    }

}
