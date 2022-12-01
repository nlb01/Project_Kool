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

        All_ingredients
        All_types

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        Handler().postDelayed({
            intent = Intent(this, HomePageCompose::class.java);
            intent.putExtra("type", "none")
            intent.putExtra("bookdmark" , false)
            startActivity(intent);}
         , 3000);

    }
}