package com.example.objecttensor

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.YuvImage
import com.otaliastudios.cameraview.frame.Frame
import java.util.concurrent.LinkedBlockingQueue

class Configuration(private val listener: TensorFlowListener) {

    private var queue = LinkedBlockingQueue<YuvImage>()
    private var state: Boolean = false
    private var classifier: Classifier? = null


    fun init(
        context: Context,
        modelName: String,
        labelsName: String,
        inputSize: Int,
        isQuant: Boolean
    ){
        this.classifier = TFLiteObjectDetectionAPIModel.create(
            context.assets,
            modelName,
            labelsName,
            inputSize,
            isQuant
        )


    }


    fun getData(frame: Frame) {
        val yuvImage =
            YuvImage(frame.data, ImageFormat.NV21, frame.size.width, frame.size.height, null)



        if (!state) {
            CustomThread(queue, classifier!! ,listener).start()
            state = true
        }
        onCameraFrame(yuvImage)
    }


    private fun onCameraFrame(yuv: YuvImage) {
        queue.put(yuv)

    }


}