package com.example.objecttensor

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity(), TensorFlowListener {


    var stamp = 0
    lateinit var configuration: Configuration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraView.setLifecycleOwner(this)

        configuration = Configuration(this)

        try {
            configuration.init(
                this,
                "detect.tflite",
                "file:///android_asset/labelmap.txt",
                300,
                true
            )
            cameraView.addFrameProcessor {

                configuration.getData(it)


            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(javaClass.simpleName, "error")
            val toast = Toast.makeText(
                applicationContext,
                "Classifier could not be initialized",
                Toast.LENGTH_SHORT
            )
            toast.show()
            finish()
        }
    }

    private fun getPermission(): Boolean {
        var state = false
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    state = true
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        return state
    }


    override fun OnSuccessListener(yuvImage: List<Recognition?>?) {
        Log.e(javaClass.simpleName, yuvImage.toString())
    }


}


