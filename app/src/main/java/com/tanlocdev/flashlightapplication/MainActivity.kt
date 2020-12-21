package com.tanlocdev.flashlightapplication


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    //spring anim
    private companion object Params {
        val STIFFNESS = SpringForce.STIFFNESS_MEDIUM
        val DAMPING_RATIO = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    }
    lateinit var xAnimation: SpringAnimation
    lateinit var yAnimation: SpringAnimation
    private val CAMERA_REQUEST = 50
    private var flashLightStatus = false
    private val RECORD_REQUEST_CODE = 101
    private var camera: Camera? = null
    lateinit var params: Camera.Parameters
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    private var  stringForce : SpringForce?= null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val anim :Animation = AnimationUtils.loadAnimation(this, R.anim.fadedown)
        window.setStatusBarColor(this.getResources().getColor(R.color.blackOFF))
        view2.startAnimation(anim)
        view.startAnimation(anim)
        imageView.startAnimation(anim)
        imageView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                xAnimation = createSpringAnimation(
                    imageView, SpringAnimation.X, imageView.x, STIFFNESS, DAMPING_RATIO
                )
                yAnimation = createSpringAnimation(
                    imageView, SpringAnimation.Y, imageView.y, STIFFNESS, DAMPING_RATIO
                )
                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        // scale hình ảnh
        val scale = ScaleAnimation(
            0F,
            1F,
            0F,
            1F,
            ScaleAnimation.RELATIVE_TO_SELF,
            .5f,
            ScaleAnimation.RELATIVE_TO_SELF,
            .5f
        )
        scale.duration = 500
        scale.interpolator = OvershootInterpolator()



        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                xAnimation = createSpringAnimation(
                    view, SpringAnimation.X, view.x, STIFFNESS, DAMPING_RATIO
                )
                yAnimation = createSpringAnimation(
                    view, SpringAnimation.Y, view.y, STIFFNESS, DAMPING_RATIO
                )
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        textView.startAnimation(scale)
        textView.setOnClickListener {
            textView.startAnimation(scale)
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        var dX = 0f
        var dY = 0f
        val window: Window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // check flash
        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager!!.cameraIdList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!isFlashAvailable) {
            showNoFlashError()
        }
        imageView.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    // capture the difference between view's top left corner and touch point
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY

                    // cancel animations so we can grab the view during previous animation
                    xAnimation.cancel()
                    yAnimation.cancel()
                }
                MotionEvent.ACTION_MOVE -> {
                    //  a different approach would be to change the view's LayoutParams.
                    imageView.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    xAnimation.start()
                    yAnimation.start()
                    if (flashLightStatus == false) {
                        Dexter.withActivity(this)
                            .withPermission(Manifest.permission.CAMERA)
                            .withListener(object : PermissionListener {
                                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                    FlashOn()
                                }

                                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                    // check for permanent denial of permission
                                    if (response.isPermanentlyDenied) {
                                        // navigate user to app settings
                                    }
                                }

                                override fun onPermissionRationaleShouldBeShown(
                                    permission: PermissionRequest?,
                                    token: PermissionToken
                                ) {
                                    token.continuePermissionRequest()
                                }
                            }).check()
                    } else {
                        FlashOff()
                    }

                }
            }
            true
        }
    }
    fun createSpringAnimation(
        view: View,
        property: DynamicAnimation.ViewProperty,
        finalPosition: Float,
        stiffness: Float,
        dampingRatio: Float
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = dampingRatio
        animation.spring = spring
        return animation
    }
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun FlashOn() {

            imageView.setBackgroundResource(R.drawable.bg3)
            contraislayout.setBackgroundColor(this.getResources().getColor(R.color.yellowOn))
            window.setStatusBarColor(this.getResources().getColor(R.color.yellowOn))
            view2.setBackgroundResource(R.drawable.bg_rot2)
            view.setBackgroundColor(Color.parseColor("#000000"))
            flashLightStatus = true

            try{
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
                {
                    if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
                    {
                        for (camID in mCameraManager!!.cameraIdList) {
                            val cameraCharacteristics = mCameraManager!!.getCameraCharacteristics(
                                camID
                            )
                            val lensFacing =
                                cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)!!
                            if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT
                                && cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!
                            ) {
                                mCameraId = camID
                                break
                            } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK
                                && cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!
                            ) {
                                mCameraId = camID
                            }
                            if (mCameraId != null) {
                                mCameraManager!!.setTorchMode(mCameraId!!, true)
                            }
                        }
                    }
                }

            }
            catch (e: Exception)
            {
                Log.d("Errol turn on flash", e.message.toString())
            }


        }
    private fun getCamera() {

        if (camera == null) {
            try {
                camera = Camera.open()
                params = camera!!.parameters
            } catch (e: RuntimeException) {
                Log.e("Failed to Open. Error: ", e.message.toString())
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStop() {
        super.onStop()
        FlashOff()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDestroy() {
        super.onDestroy()
        FlashOff()
    }

    override fun onStart() {
        super.onStart()
        //getCamera()
    }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun FlashOff() {
            imageView.setBackgroundResource(R.drawable.bg2)
            contraislayout.setBackgroundColor(this.getResources().getColor(R.color.blackOFF))
            window.setStatusBarColor(this.getResources().getColor(R.color.blackOFF))
            view2.setBackgroundResource(R.drawable.bg_rot)
            view.setBackgroundColor(Color.parseColor("#ffffff"))
            flashLightStatus = false

            try{
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
                {
                    for (camID in mCameraManager!!.cameraIdList) {
                        val cameraCharacteristics = mCameraManager!!.getCameraCharacteristics(camID)
                        val lensFacing =
                            cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)!!
                        if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT
                            && cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!
                        ) {
                            mCameraId = camID
                            break
                        } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK
                            && cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!
                        ) {
                            mCameraId = camID
                        }
                        if (mCameraId != null) {
                            mCameraManager!!.setTorchMode(mCameraId!!, false)
                        }
                    }
                }

            }
            catch (e: Exception)
            {
                Log.d("Errol turn on flash", e.message.toString())
            }

        }
    fun showNoFlashError() {
        val alert: AlertDialog = AlertDialog.Builder(this)
            .create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(
            DialogInterface.BUTTON_POSITIVE,
            "OK",
            DialogInterface.OnClickListener { dialog, which -> finish() })
        alert.show()
    }

}