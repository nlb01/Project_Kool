package com.example.koolkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

//        val db = DBhelper(this, null)
//
//        Toast.makeText(this, "Database Created and Populated", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            intent = Intent(this, HomePageCompose::class.java);
            startActivity(intent);}
         , 3000);

    }
}