package com.example.koolkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        All_ingredients
        All_types

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        Handler().postDelayed({
            val ingreds = listOf(-1)
            intent = Intent(this, HomePageCompose::class.java);
            intent.putExtra("type", "none")
            intent.putExtra("bookdmark" , false)
            intent.putExtra("ingredList", ingreds.toIntArray())
            startActivity(intent);}
         , 3000);

    }
}