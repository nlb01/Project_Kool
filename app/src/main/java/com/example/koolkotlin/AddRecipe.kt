package com.example.koolkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AddRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    fun addActivity(view : View) {
        intent = Intent(this, AddRecipe::class.java);
        startActivity(intent);
    }

    fun details(view: View) {
        intent = Intent(this, RecipeDetails::class.java);
        startActivity(intent);
    }
}