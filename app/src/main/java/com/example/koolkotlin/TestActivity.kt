package com.example.koolkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Log.i("bitmap" , "In next activity")
        val bmp:Bitmap
        val arr = intent.getByteArrayExtra("title")
        if(arr?.size == null) {
            Log.i("bitmap" , "Got array in Test Activity and it's null")
            bmp = BitmapFactory.decodeByteArray(arr, 0 , 0)
        }
        else {
            Log.i("bitmap" , "Got array in Test Activity and it's not null")
            bmp = BitmapFactory.decodeByteArray(arr, 0 , arr.size)
        }

        findViewById<ImageView>(R.id.testImage).setImageBitmap(bmp)
    }
}