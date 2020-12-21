package com.tanlocdev.flashlightapplication


import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity2 : AppCompatActivity() {
    var textCode: String = ""
    private var flashLightStatus = false
    private val RECORD_REQUEST_CODE = 101
    private var camera: Camera? = null
    lateinit var params: Camera.Parameters
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null

    //custo dialog
    internal var customDialog: CustomListViewDialog? = null

    private val tutorialData =  TutorialAdapter(
            listOf(
                Tutorial(
                    "A",
                    ".-"
                ),
                Tutorial(
                    "B",
                    "-..."
                ),
                Tutorial(
                    "C",
                    "-.-."
                ),
                Tutorial(
                    "D",
                    "-.."
                ),
                Tutorial(
                    "E",
                    "."
                ),
                Tutorial(
                    "F",
                    "..-."
                ),
                Tutorial(
                    "G",
                    "--."
                ),
                Tutorial(
                    "H",
                    "...."
                ),
                Tutorial(
                    "I",
                    ".."
                ),
                Tutorial(
                    "J",
                    ".---"
                ),
                Tutorial(
                    "K",
                    "-.-"
                ),
                Tutorial(
                    "L",
                    ".-.."
                ),
                Tutorial(
                    "M",
                    "--"
                ),
                Tutorial(
                    "N",
                    "-."
                ),
                Tutorial(
                    "O",
                    "---"
                ),
                Tutorial(
                    "P",
                    ".--."
                ),
                Tutorial(
                    "Q",
                    "--.-"
                ),
                Tutorial(
                    "R",
                    ".-."
                ),
                Tutorial(
                    "S",
                    "..."
                ),
                Tutorial(
                    "T",
                    "-"
                ),
                Tutorial(
                    "U",
                    "..-"
                ),
                Tutorial(
                    "V",
                    "...-"
                ),
                Tutorial(
                    "W",
                    ".--"
                ),
                Tutorial(
                    "X",
                    "-..-"
                ),
                Tutorial(
                    "Y",
                    "-.--"
                ),
                Tutorial(
                    "Z",
                    "..--"
                ),
                Tutorial(
                    "0",
                    "-----"
                ),
                Tutorial(
                    "1",
                    ".----"
                ),
                Tutorial(
                    "2",
                    "..---"
                ),
                Tutorial(
                    "3",
                    "...--"
                ),Tutorial(
                    "4",
                    "....-"
                ),
                Tutorial(
                    "5",
                    "....."
                ),Tutorial(
                    "6",
                    "-...."
                ),Tutorial(
                    "7",
                    "--..."
                ),Tutorial(
                    "8",
                    "---.."
                ),
                Tutorial(
                    "9",
                    "----."
                )
            )
    )
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Log.d("check","hien ra go do")
        //set recyceview dialog
        customDialog = CustomListViewDialog(this@MainActivity2,tutorialData)
        customDialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        customDialog!!.setCanceledOnTouchOutside(true)

        val animtion_custom: Animation = AnimationUtils.loadAnimation(this,R.anim.custom_anim)

        button_Frame.startAnimation(animtion_custom)
        textView2.startAnimation(animtion_custom)
        textView3.startAnimation(animtion_custom)
        editTextTextMultiLine.startAnimation(animtion_custom)
        textView4.startAnimation(animtion_custom)
        editTextTextMultiLine2.startAnimation(animtion_custom)
        cvButton.startAnimation(animtion_custom)
        button.startAnimation(animtion_custom)
        customDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        button_Frame.setOnClickListener {
            customDialog!!.show()
        }



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
        scale.duration = 700
        scale.interpolator = OvershootInterpolator()

        window.setStatusBarColor(this.getResources().getColor(R.color.blackOFF))
        textView2.setOnClickListener {
            textView2.startAnimation(scale)
            finish()
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        //scale hinh button
        // scale hình ảnh


        // check flash
        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager!!.cameraIdList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        button.setOnClickListener {
            button.startAnimation(scale)
            if (!editTextTextMultiLine.text.isNullOrEmpty())
            {
                var myStringBufferLoc = editTextTextMultiLine.text.toString()

                val myString = textCode
                val blinkDelay: Long = 2000 //Delay in ms
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
                    for (i in 0 until myString.length) {
                        if (myString[i] == '-') {
                            turnOnOff(true)
                            Thread.sleep(800)
                            turnOnOff(false)
                        }
                        if (myString[i] == '.'){
                            turnOnOff(true)
                            Thread.sleep(500)
                            turnOnOff(false)
                        }
                        else{
                            Thread.sleep(1000);
                        }
                    }
                }
            }
            else{
                Toast.makeText(this,"Your text is invaild",Toast.LENGTH_SHORT).show()
            }




        }
        cvButton.setOnClickListener {
            cvButton.startAnimation(scale)
            if (!editTextTextMultiLine.text.isNullOrEmpty())
            {
                textToMorse(editTextTextMultiLine.text.toString())
            }
            else{
                Toast.makeText(this,"Your text is invan",Toast.LENGTH_SHORT).show()
            }
        }


    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun turnOnOff(check: Boolean)
    {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
        for (camID in mCameraManager!!.cameraIdList) {
            val cameraCharacteristics =
                mCameraManager!!.getCameraCharacteristics(camID)
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
                mCameraManager!!.setTorchMode(mCameraId!!, check)
            }
        }
    }
    }
    fun textToMorse(s: String) : String {
        val morseArr = s.map{value -> convertCharToMorse(value)}
        println(morseArr.joinToString(" "))
        Toast.makeText(this,morseArr.toString(),Toast.LENGTH_SHORT).show()
        editTextTextMultiLine2.setText(morseArr.joinToString(" "))
        textCode = morseArr.joinToString(" ")
        return s
    }
    fun onClick(view: View)
    {

    }




    fun convertCharToMorse(c : Char) = when (c.toLowerCase()) {
        'a' -> ".-"
        'b' -> "-..."
        'c' -> "-.-."
        'd' -> "-.."
        'e' -> "."
        'f' -> "..-."
        'g' -> "--."
        'h' -> "...."
        'i' -> ".."
        'j' -> ".---"
        'k' -> "-.-"
        'l' -> ".-.."
        'm' -> "--"
        'n' -> "-."
        'o' -> "---"
        'p' -> ".--."
        'q' -> "--.-"
        'r' -> ".-."
        's' -> "..."
        't' -> "-"
        'u' -> "..-"
        'v' -> "...-"
        'w' -> ".--"
        'x' -> "-..-"
        'y' -> "-.--"
        'z' -> "--.."
        '1' -> ".----"
        '2' -> "..---"
        '3' -> "...--"
        '4' -> "....-"
        '5' -> "....."
        '6' -> "-...."
        '7' -> "--..."
        '8' -> "---.."
        '9' -> "----."
        '0' -> "-----"
        ' ' -> " / "
        else -> throw IllegalArgumentException("Not all characters are valid")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}